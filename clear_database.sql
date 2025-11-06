-- 清空数据库脚本
-- 注意：此脚本会删除所有数据，请谨慎使用！
-- 使用方法：在MySQL客户端中执行此脚本

USE lab_equipment;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 按依赖关系顺序删除数据
TRUNCATE TABLE borrow_transaction;
TRUNCATE TABLE consumable_issue_item;
TRUNCATE TABLE consumable_issue;
TRUNCATE TABLE consumable_requisition_item;
TRUNCATE TABLE consumable_requisition;
TRUNCATE TABLE inventory;
TRUNCATE TABLE consumable;
TRUNCATE TABLE scrap_record;
TRUNCATE TABLE repair_record;
TRUNCATE TABLE asset_ledger;
TRUNCATE TABLE device;
TRUNCATE TABLE device_category;
TRUNCATE TABLE approval;
TRUNCATE TABLE purchase_request;
TRUNCATE TABLE purchase_plan_item;
TRUNCATE TABLE purchase_plan;
TRUNCATE TABLE laboratory;
TRUNCATE TABLE user_role;
TRUNCATE TABLE user_account;
TRUNCATE TABLE role;
TRUNCATE TABLE department;
TRUNCATE TABLE supplier;
TRUNCATE TABLE audit_log;

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 显示清空结果
SELECT '数据库已清空，可以重新启动应用以初始化测试数据' AS message;

