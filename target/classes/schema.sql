-- Database: equipment (create manually if not exists)

-- =========================
-- Base & Organization
-- =========================
CREATE TABLE IF NOT EXISTS department (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  code VARCHAR(50) UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS laboratory (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  department_id BIGINT NOT NULL,
  manager_user_id BIGINT,
  location VARCHAR(200),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_lab_dept FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE TABLE IF NOT EXISTS user_account (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(80) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(120),
  email VARCHAR(160),
  department_id BIGINT,
  enabled TINYINT(1) DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_user_dept FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE TABLE IF NOT EXISTS role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(60) UNIQUE NOT NULL,
  description VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS user_role (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES user_account(id),
  CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE IF NOT EXISTS supplier (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(160) NOT NULL,
  contact VARCHAR(120),
  phone VARCHAR(40),
  address VARCHAR(255)
);

-- =========================
-- Devices & Assets
-- =========================
CREATE TABLE IF NOT EXISTS device_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  code VARCHAR(60) UNIQUE
);

CREATE TABLE IF NOT EXISTS device (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  category_id BIGINT NOT NULL,
  laboratory_id BIGINT NOT NULL,
  class_name VARCHAR(120),
  device_name VARCHAR(160) NOT NULL,
  device_code VARCHAR(100) UNIQUE NOT NULL,
  serial_no VARCHAR(120),
  spec VARCHAR(200),
  unit_price DECIMAL(12,2),
  quantity INT DEFAULT 1,
  purchase_date DATE,
  manufacturer VARCHAR(160),
  warranty_until DATE,
  handler_user_id BIGINT,
  status VARCHAR(40) DEFAULT 'IN_STOCK', -- IN_STOCK/IN_USE/UNDER_REPAIR/SCRAPPED
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_dev_cat FOREIGN KEY (category_id) REFERENCES device_category(id),
  CONSTRAINT fk_dev_lab FOREIGN KEY (laboratory_id) REFERENCES laboratory(id),
  CONSTRAINT fk_dev_handler FOREIGN KEY (handler_user_id) REFERENCES user_account(id)
);

CREATE TABLE IF NOT EXISTS asset_ledger (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  device_id BIGINT NOT NULL,
  asset_code VARCHAR(100) UNIQUE NOT NULL,
  original_value DECIMAL(14,2),
  booked_at DATE,
  depreciation_method VARCHAR(40) DEFAULT 'NONE',
  location VARCHAR(200),
  keeper_user_id BIGINT,
  status VARCHAR(40) DEFAULT 'ACTIVE', -- ACTIVE/INACTIVE/SCRAPPED
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_asset_device FOREIGN KEY (device_id) REFERENCES device(id),
  CONSTRAINT fk_asset_keeper FOREIGN KEY (keeper_user_id) REFERENCES user_account(id)
);

-- =========================
-- Approvals (Unified Workflow)
-- =========================
CREATE TABLE IF NOT EXISTS approval (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  biz_type VARCHAR(60) NOT NULL, -- PLAN/PURCHASE_REQ/SCRAP/CONSUMABLE_REQ
  biz_id BIGINT NOT NULL,
  node VARCHAR(80) NOT NULL,
  approver_user_id BIGINT NOT NULL,
  result VARCHAR(20) NOT NULL, -- PENDING/APPROVED/REJECTED
  comment VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  acted_at TIMESTAMP NULL,
  CONSTRAINT fk_approval_user FOREIGN KEY (approver_user_id) REFERENCES user_account(id)
);

-- =========================
-- Purchase Planning & Requests
-- =========================
CREATE TABLE IF NOT EXISTS purchase_plan (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  semester VARCHAR(40) NOT NULL,
  laboratory_id BIGINT NOT NULL,
  submitter_user_id BIGINT NOT NULL,
  status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT/SUBMITTED/APPROVED/REJECTED
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_pp_lab FOREIGN KEY (laboratory_id) REFERENCES laboratory(id),
  CONSTRAINT fk_pp_submitter FOREIGN KEY (submitter_user_id) REFERENCES user_account(id)
);

CREATE TABLE IF NOT EXISTS purchase_plan_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  plan_id BIGINT NOT NULL,
  item_name VARCHAR(160) NOT NULL,
  category VARCHAR(120),
  spec VARCHAR(160),
  unit VARCHAR(40),
  planned_qty INT NOT NULL,
  est_unit_price DECIMAL(12,2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ppi_plan FOREIGN KEY (plan_id) REFERENCES purchase_plan(id)
);

CREATE TABLE IF NOT EXISTS purchase_request (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  laboratory_id BIGINT NOT NULL,
  reason VARCHAR(500) NOT NULL,
  budget DECIMAL(14,2),
  status VARCHAR(20) DEFAULT 'PENDING', -- PENDING/APPROVED/REJECTED
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_preq_lab FOREIGN KEY (laboratory_id) REFERENCES laboratory(id)
);

-- Optional: purchase order for plan -> procurement
CREATE TABLE IF NOT EXISTS purchase_order (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  plan_id BIGINT,
  supplier_id BIGINT,
  ordered_at DATE,
  status VARCHAR(20) DEFAULT 'CREATED', -- CREATED/RECEIVED/CANCELLED
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_po_plan FOREIGN KEY (plan_id) REFERENCES purchase_plan(id),
  CONSTRAINT fk_po_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

CREATE TABLE IF NOT EXISTS purchase_order_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  plan_item_id BIGINT,
  item_name VARCHAR(160) NOT NULL,
  spec VARCHAR(160),
  unit VARCHAR(40),
  qty INT NOT NULL,
  unit_price DECIMAL(12,2) NOT NULL,
  received_qty INT DEFAULT 0,
  CONSTRAINT fk_poi_order FOREIGN KEY (order_id) REFERENCES purchase_order(id),
  CONSTRAINT fk_poi_plan_item FOREIGN KEY (plan_item_id) REFERENCES purchase_plan_item(id)
);

-- =========================
-- Borrow & Return
-- =========================
CREATE TABLE IF NOT EXISTS borrow_transaction (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  device_id BIGINT NOT NULL,
  borrower_user_id BIGINT NOT NULL,
  borrowed_at DATETIME NOT NULL,
  due_at DATETIME NOT NULL,
  returned_at DATETIME NULL,
  status VARCHAR(20) DEFAULT 'BORROWED', -- BORROWED/RETURNED/OVERDUE
  checkout_operator_id BIGINT,
  return_operator_id BIGINT,
  CONSTRAINT fk_borrow_device FOREIGN KEY (device_id) REFERENCES device(id),
  CONSTRAINT fk_borrow_user FOREIGN KEY (borrower_user_id) REFERENCES user_account(id),
  CONSTRAINT fk_borrow_out_op FOREIGN KEY (checkout_operator_id) REFERENCES user_account(id),
  CONSTRAINT fk_borrow_in_op FOREIGN KEY (return_operator_id) REFERENCES user_account(id)
);

-- =========================
-- Repair & Scrap
-- =========================
CREATE TABLE IF NOT EXISTS repair_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  device_id BIGINT NOT NULL,
  problem_desc VARCHAR(500) NOT NULL,
  report_date DATE NOT NULL,
  fixed_date DATE,
  repair_vendor VARCHAR(160),
  cost DECIMAL(12,2),
  responsible_user_id BIGINT,
  result VARCHAR(120),
  CONSTRAINT fk_repair_device FOREIGN KEY (device_id) REFERENCES device(id),
  CONSTRAINT fk_repair_resp FOREIGN KEY (responsible_user_id) REFERENCES user_account(id)
);

CREATE TABLE IF NOT EXISTS scrap_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  device_id BIGINT NOT NULL,
  scrap_date DATE NOT NULL,
  reason VARCHAR(400),
  approval_id BIGINT,
  disposal VARCHAR(160),
  CONSTRAINT fk_scrap_device FOREIGN KEY (device_id) REFERENCES device(id)
);

-- =========================
-- Consumables & Inventory
-- =========================
CREATE TABLE IF NOT EXISTS consumable (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(160) NOT NULL,
  category VARCHAR(120),
  spec VARCHAR(160),
  unit VARCHAR(40),
  supplier_id BIGINT,
  unit_price DECIMAL(12,2),
  CONSTRAINT fk_cons_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

CREATE TABLE IF NOT EXISTS inventory (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  item_id BIGINT NOT NULL,
  laboratory_id BIGINT, -- null means central warehouse
  quantity INT NOT NULL DEFAULT 0,
  safety_stock INT DEFAULT 0,
  UNIQUE KEY uq_inv_item_lab (item_id, laboratory_id),
  CONSTRAINT fk_inv_item FOREIGN KEY (item_id) REFERENCES consumable(id),
  CONSTRAINT fk_inv_lab FOREIGN KEY (laboratory_id) REFERENCES laboratory(id)
);

CREATE TABLE IF NOT EXISTS consumable_requisition (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  laboratory_id BIGINT NOT NULL,
  reason VARCHAR(400) NOT NULL,
  status VARCHAR(20) DEFAULT 'PENDING', -- PENDING/APPROVED/REJECTED
  approval_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_cr_lab FOREIGN KEY (laboratory_id) REFERENCES laboratory(id)
);

CREATE TABLE IF NOT EXISTS consumable_requisition_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  requisition_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  qty INT NOT NULL,
  reason VARCHAR(400),
  CONSTRAINT fk_cri_req FOREIGN KEY (requisition_id) REFERENCES consumable_requisition(id),
  CONSTRAINT fk_cri_item FOREIGN KEY (item_id) REFERENCES consumable(id)
);

CREATE TABLE IF NOT EXISTS consumable_issue (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  laboratory_id BIGINT NOT NULL,
  issuer_user_id BIGINT NOT NULL,
  issue_date DATE NOT NULL,
  related_plan_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ci_lab FOREIGN KEY (laboratory_id) REFERENCES laboratory(id),
  CONSTRAINT fk_ci_user FOREIGN KEY (issuer_user_id) REFERENCES user_account(id)
);

CREATE TABLE IF NOT EXISTS consumable_issue_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  issue_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  qty INT NOT NULL,
  unit_price DECIMAL(12,2),
  in_plan TINYINT(1) DEFAULT 1,
  plan_item_id BIGINT,
  CONSTRAINT fk_cii_issue FOREIGN KEY (issue_id) REFERENCES consumable_issue(id),
  CONSTRAINT fk_cii_item FOREIGN KEY (item_id) REFERENCES consumable(id),
  CONSTRAINT fk_cii_plan_item FOREIGN KEY (plan_item_id) REFERENCES purchase_plan_item(id)
);

-- =========================
-- Reporting & Audit
-- =========================
CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  table_name VARCHAR(120) NOT NULL,
  record_id VARCHAR(120) NOT NULL,
  action VARCHAR(20) NOT NULL,
  actor_user_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  snapshot LONGTEXT NULL
);

-- Optional aggregated view/table can be built later


