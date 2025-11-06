package com.example.equipment.config;

import com.example.equipment.model.*;
import com.example.equipment.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.data.force-init:false}")
    private boolean forceInit;

    private final DepartmentRepository departmentRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final UserAccountRepository userAccountRepository;
    private final DeviceCategoryRepository deviceCategoryRepository;
    private final DeviceRepository deviceRepository;
    private final ConsumableRepository consumableRepository;
    private final InventoryRepository inventoryRepository;
    private final BorrowTransactionRepository borrowRepository;

    public DataInitializer(
            DepartmentRepository departmentRepository,
            LaboratoryRepository laboratoryRepository,
            UserAccountRepository userAccountRepository,
            DeviceCategoryRepository deviceCategoryRepository,
            DeviceRepository deviceRepository,
            ConsumableRepository consumableRepository,
            InventoryRepository inventoryRepository,
            BorrowTransactionRepository borrowRepository) {
        this.departmentRepository = departmentRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.userAccountRepository = userAccountRepository;
        this.deviceCategoryRepository = deviceCategoryRepository;
        this.deviceRepository = deviceRepository;
        this.consumableRepository = consumableRepository;
        this.inventoryRepository = inventoryRepository;
        this.borrowRepository = borrowRepository;
    }

    @Override
    public void run(String... args) {
        // 如果强制初始化，先清空现有数据
        if (forceInit) {
            System.out.println("强制初始化模式：清空现有测试数据...");
            borrowRepository.deleteAll();
            inventoryRepository.deleteAll();
            consumableRepository.deleteAll();
            deviceRepository.deleteAll();
            deviceCategoryRepository.deleteAll();
            laboratoryRepository.deleteAll();
            userAccountRepository.deleteAll();
            departmentRepository.deleteAll();
            System.out.println("现有数据已清空");
        } else if (userAccountRepository.findByUsername("admin").isPresent()) {
            System.out.println("测试数据已存在，跳过初始化");
            System.out.println("提示：如需重新初始化，请在 application.yml 中设置 app.data.force-init: true");
            return;
        }

        System.out.println("开始初始化测试数据...");

        // 1. 创建院系
        Department dept1 = new Department();
        dept1.setName("计算机学院");
        dept1.setCode("CS");
        dept1.setCreatedAt(LocalDateTime.now());
        dept1.setUpdatedAt(LocalDateTime.now());
        dept1 = departmentRepository.save(dept1);

        Department dept2 = new Department();
        dept2.setName("物理学院");
        dept2.setCode("PHY");
        dept2.setCreatedAt(LocalDateTime.now());
        dept2.setUpdatedAt(LocalDateTime.now());
        dept2 = departmentRepository.save(dept2);

        Department dept3 = new Department();
        dept3.setName("化学学院");
        dept3.setCode("CHEM");
        dept3.setCreatedAt(LocalDateTime.now());
        dept3.setUpdatedAt(LocalDateTime.now());
        dept3 = departmentRepository.save(dept3);

        Department dept4 = new Department();
        dept4.setName("生物学院");
        dept4.setCode("BIO");
        dept4.setCreatedAt(LocalDateTime.now());
        dept4.setUpdatedAt(LocalDateTime.now());
        dept4 = departmentRepository.save(dept4);

        // 2. 创建用户（包括默认管理员）
        UserAccount admin = new UserAccount();
        admin.setUsername("admin");
        admin.setPassword("{noop}admin123"); // 使用 {noop} 前缀表示明文密码
        admin.setFullName("系统管理员");
        admin.setEmail("admin@labe.edu");
        admin.setDepartmentId(dept1.getId());
        admin.setEnabled(true);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        admin = userAccountRepository.save(admin);

        UserAccount teacher1 = new UserAccount();
        teacher1.setUsername("teacher1");
        teacher1.setPassword("{noop}123456");
        teacher1.setFullName("张老师");
        teacher1.setEmail("teacher1@labe.edu");
        teacher1.setDepartmentId(dept1.getId());
        teacher1.setEnabled(true);
        teacher1.setCreatedAt(LocalDateTime.now());
        teacher1.setUpdatedAt(LocalDateTime.now());
        teacher1 = userAccountRepository.save(teacher1);

        UserAccount teacher2 = new UserAccount();
        teacher2.setUsername("teacher2");
        teacher2.setPassword("{noop}123456");
        teacher2.setFullName("李老师");
        teacher2.setEmail("teacher2@labe.edu");
        teacher2.setDepartmentId(dept2.getId());
        teacher2.setEnabled(true);
        teacher2.setCreatedAt(LocalDateTime.now());
        teacher2.setUpdatedAt(LocalDateTime.now());
        teacher2 = userAccountRepository.save(teacher2);

        UserAccount teacher3 = new UserAccount();
        teacher3.setUsername("teacher3");
        teacher3.setPassword("{noop}123456");
        teacher3.setFullName("王老师");
        teacher3.setEmail("teacher3@labe.edu");
        teacher3.setDepartmentId(dept3.getId());
        teacher3.setEnabled(true);
        teacher3.setCreatedAt(LocalDateTime.now());
        teacher3.setUpdatedAt(LocalDateTime.now());
        teacher3 = userAccountRepository.save(teacher3);

        UserAccount teacher4 = new UserAccount();
        teacher4.setUsername("teacher4");
        teacher4.setPassword("{noop}123456");
        teacher4.setFullName("赵老师");
        teacher4.setEmail("teacher4@labe.edu");
        teacher4.setDepartmentId(dept4.getId());
        teacher4.setEnabled(true);
        teacher4.setCreatedAt(LocalDateTime.now());
        teacher4.setUpdatedAt(LocalDateTime.now());
        teacher4 = userAccountRepository.save(teacher4);

        // 3. 创建实验室
        Laboratory lab1 = new Laboratory();
        lab1.setName("网络实验室");
        lab1.setDepartmentId(dept1.getId());
        lab1.setManagerUserId(teacher1.getId());
        lab1.setLocation("教学楼A-301");
        lab1.setCreatedAt(LocalDateTime.now());
        lab1.setUpdatedAt(LocalDateTime.now());
        lab1 = laboratoryRepository.save(lab1);

        Laboratory lab2 = new Laboratory();
        lab2.setName("物理实验中心");
        lab2.setDepartmentId(dept2.getId());
        lab2.setManagerUserId(teacher2.getId());
        lab2.setLocation("实验楼B-205");
        lab2.setCreatedAt(LocalDateTime.now());
        lab2.setUpdatedAt(LocalDateTime.now());
        lab2 = laboratoryRepository.save(lab2);

        Laboratory lab3 = new Laboratory();
        lab3.setName("化学分析实验室");
        lab3.setDepartmentId(dept3.getId());
        lab3.setManagerUserId(teacher3.getId());
        lab3.setLocation("实验楼C-101");
        lab3.setCreatedAt(LocalDateTime.now());
        lab3.setUpdatedAt(LocalDateTime.now());
        lab3 = laboratoryRepository.save(lab3);

        Laboratory lab4 = new Laboratory();
        lab4.setName("生物技术实验室");
        lab4.setDepartmentId(dept4.getId());
        lab4.setManagerUserId(teacher4.getId());
        lab4.setLocation("实验楼D-302");
        lab4.setCreatedAt(LocalDateTime.now());
        lab4.setUpdatedAt(LocalDateTime.now());
        lab4 = laboratoryRepository.save(lab4);

        // 4. 创建设备类别
        DeviceCategory cat1 = new DeviceCategory();
        cat1.setName("通用设备");
        cat1.setCode("GEN");
        cat1 = deviceCategoryRepository.save(cat1);

        DeviceCategory cat2 = new DeviceCategory();
        cat2.setName("测量仪器");
        cat2.setCode("MEAS");
        cat2 = deviceCategoryRepository.save(cat2);

        DeviceCategory cat3 = new DeviceCategory();
        cat3.setName("计算机设备");
        cat3.setCode("COMP");
        cat3 = deviceCategoryRepository.save(cat3);

        DeviceCategory cat4 = new DeviceCategory();
        cat4.setName("分析仪器");
        cat4.setCode("ANAL");
        cat4 = deviceCategoryRepository.save(cat4);

        DeviceCategory cat5 = new DeviceCategory();
        cat5.setName("光学设备");
        cat5.setCode("OPT");
        cat5 = deviceCategoryRepository.save(cat5);

        DeviceCategory cat6 = new DeviceCategory();
        cat6.setName("电子设备");
        cat6.setCode("ELEC");
        cat6 = deviceCategoryRepository.save(cat6);

        DeviceCategory cat7 = new DeviceCategory();
        cat7.setName("机械加工设备");
        cat7.setCode("MECH");
        cat7 = deviceCategoryRepository.save(cat7);

        // 5. 创建设备
        Device device1 = new Device();
        device1.setCategoryId(cat1.getId());
        device1.setLaboratoryId(lab1.getId());
        device1.setClassName("通用类");
        device1.setDeviceName("示波器");
        device1.setDeviceCode("DEV-001");
        device1.setSerialNo("SN-2024-001");
        device1.setSpec("100MHz双通道");
        device1.setUnitPrice(new BigDecimal("12000.00"));
        device1.setQuantity(1);
        device1.setPurchaseDate(LocalDate.now().minusMonths(6));
        device1.setManufacturer("泰克科技");
        device1.setWarrantyUntil(LocalDate.now().plusYears(2));
        device1.setHandlerUserId(admin.getId());
        device1.setStatus("IN_STOCK");
        device1.setCreatedAt(LocalDateTime.now());
        device1.setUpdatedAt(LocalDateTime.now());
        device1 = deviceRepository.save(device1);

        Device device2 = new Device();
        device2.setCategoryId(cat2.getId());
        device2.setLaboratoryId(lab1.getId());
        device2.setClassName("测量类");
        device2.setDeviceName("数字万用表");
        device2.setDeviceCode("DEV-002");
        device2.setSerialNo("SN-2024-002");
        device2.setSpec("4.5位精度");
        device2.setUnitPrice(new BigDecimal("800.00"));
        device2.setQuantity(5);
        device2.setPurchaseDate(LocalDate.now().minusMonths(3));
        device2.setManufacturer("福禄克");
        device2.setWarrantyUntil(LocalDate.now().plusYears(1));
        device2.setHandlerUserId(teacher1.getId());
        device2.setStatus("IN_STOCK");
        device2.setCreatedAt(LocalDateTime.now());
        device2.setUpdatedAt(LocalDateTime.now());
        device2 = deviceRepository.save(device2);

        Device device3 = new Device();
        device3.setCategoryId(cat3.getId());
        device3.setLaboratoryId(lab2.getId());
        device3.setClassName("计算机类");
        device3.setDeviceName("高性能工作站");
        device3.setDeviceCode("DEV-003");
        device3.setSerialNo("SN-2024-003");
        device3.setSpec("Intel i9, 32GB RAM, RTX 4090");
        device3.setUnitPrice(new BigDecimal("25000.00"));
        device3.setQuantity(1);
        device3.setPurchaseDate(LocalDate.now().minusMonths(1));
        device3.setManufacturer("戴尔");
        device3.setWarrantyUntil(LocalDate.now().plusYears(3));
        device3.setHandlerUserId(teacher2.getId());
        device3.setStatus("IN_STOCK");
        device3.setCreatedAt(LocalDateTime.now());
        device3.setUpdatedAt(LocalDateTime.now());
        device3 = deviceRepository.save(device3);

        // 添加更多设备
        Device device4 = new Device();
        device4.setCategoryId(cat4.getId());
        device4.setLaboratoryId(lab2.getId());
        device4.setClassName("分析类");
        device4.setDeviceName("光谱分析仪");
        device4.setDeviceCode("DEV-004");
        device4.setSerialNo("SN-2024-004");
        device4.setSpec("紫外可见光谱，波长范围200-800nm");
        device4.setUnitPrice(new BigDecimal("45000.00"));
        device4.setQuantity(1);
        device4.setPurchaseDate(LocalDate.now().minusMonths(4));
        device4.setManufacturer("岛津");
        device4.setWarrantyUntil(LocalDate.now().plusYears(2));
        device4.setHandlerUserId(teacher2.getId());
        device4.setStatus("IN_STOCK");
        device4.setCreatedAt(LocalDateTime.now());
        device4.setUpdatedAt(LocalDateTime.now());
        device4 = deviceRepository.save(device4);

        Device device5 = new Device();
        device5.setCategoryId(cat5.getId());
        device5.setLaboratoryId(lab2.getId());
        device5.setClassName("光学类");
        device5.setDeviceName("激光器");
        device5.setDeviceCode("DEV-005");
        device5.setSerialNo("SN-2024-005");
        device5.setSpec("532nm绿光，功率500mW");
        device5.setUnitPrice(new BigDecimal("15000.00"));
        device5.setQuantity(2);
        device5.setPurchaseDate(LocalDate.now().minusMonths(2));
        device5.setManufacturer("相干公司");
        device5.setWarrantyUntil(LocalDate.now().plusYears(1));
        device5.setHandlerUserId(teacher2.getId());
        device5.setStatus("IN_STOCK");
        device5.setCreatedAt(LocalDateTime.now());
        device5.setUpdatedAt(LocalDateTime.now());
        device5 = deviceRepository.save(device5);

        Device device6 = new Device();
        device6.setCategoryId(cat6.getId());
        device6.setLaboratoryId(lab1.getId());
        device6.setClassName("电子类");
        device6.setDeviceName("信号发生器");
        device6.setDeviceCode("DEV-006");
        device6.setSerialNo("SN-2024-006");
        device6.setSpec("函数信号发生器，频率范围1Hz-20MHz");
        device6.setUnitPrice(new BigDecimal("3500.00"));
        device6.setQuantity(3);
        device6.setPurchaseDate(LocalDate.now().minusMonths(5));
        device6.setManufacturer("普源精电");
        device6.setWarrantyUntil(LocalDate.now().plusYears(2));
        device6.setHandlerUserId(teacher1.getId());
        device6.setStatus("IN_STOCK");
        device6.setCreatedAt(LocalDateTime.now());
        device6.setUpdatedAt(LocalDateTime.now());
        device6 = deviceRepository.save(device6);

        Device device7 = new Device();
        device7.setCategoryId(cat3.getId());
        device7.setLaboratoryId(lab1.getId());
        device7.setClassName("计算机类");
        device7.setDeviceName("服务器");
        device7.setDeviceCode("DEV-007");
        device7.setSerialNo("SN-2024-007");
        device7.setSpec("双路Xeon，128GB RAM，10TB存储");
        device7.setUnitPrice(new BigDecimal("58000.00"));
        device7.setQuantity(1);
        device7.setPurchaseDate(LocalDate.now().minusMonths(8));
        device7.setManufacturer("联想");
        device7.setWarrantyUntil(LocalDate.now().plusYears(3));
        device7.setHandlerUserId(admin.getId());
        device7.setStatus("IN_STOCK");
        device7.setCreatedAt(LocalDateTime.now());
        device7.setUpdatedAt(LocalDateTime.now());
        device7 = deviceRepository.save(device7);

        Device device8 = new Device();
        device8.setCategoryId(cat2.getId());
        device8.setLaboratoryId(lab1.getId());
        device8.setClassName("测量类");
        device8.setDeviceName("温度记录仪");
        device8.setDeviceCode("DEV-008");
        device8.setSerialNo("SN-2024-008");
        device8.setSpec("8通道，-200℃~1800℃");
        device8.setUnitPrice(new BigDecimal("2800.00"));
        device8.setQuantity(4);
        device8.setPurchaseDate(LocalDate.now().minusMonths(3));
        device8.setManufacturer("安捷伦");
        device8.setWarrantyUntil(LocalDate.now().plusYears(1));
        device8.setHandlerUserId(teacher1.getId());
        device8.setStatus("IN_STOCK");
        device8.setCreatedAt(LocalDateTime.now());
        device8.setUpdatedAt(LocalDateTime.now());
        device8 = deviceRepository.save(device8);

        Device device9 = new Device();
        device9.setCategoryId(cat7.getId());
        device9.setLaboratoryId(lab2.getId());
        device9.setClassName("机械类");
        device9.setDeviceName("3D打印机");
        device9.setDeviceCode("DEV-009");
        device9.setSerialNo("SN-2024-009");
        device9.setSpec("FDM技术，打印尺寸200x200x200mm");
        device9.setUnitPrice(new BigDecimal("12000.00"));
        device9.setQuantity(2);
        device9.setPurchaseDate(LocalDate.now().minusMonths(6));
        device9.setManufacturer("创想三维");
        device9.setWarrantyUntil(LocalDate.now().plusYears(1));
        device9.setHandlerUserId(teacher2.getId());
        device9.setStatus("IN_STOCK");
        device9.setCreatedAt(LocalDateTime.now());
        device9.setUpdatedAt(LocalDateTime.now());
        device9 = deviceRepository.save(device9);

        Device device10 = new Device();
        device10.setCategoryId(cat1.getId());
        device10.setLaboratoryId(lab1.getId());
        device10.setClassName("通用类");
        device10.setDeviceName("恒温培养箱");
        device10.setDeviceCode("DEV-010");
        device10.setSerialNo("SN-2024-010");
        device10.setSpec("温度范围5-60℃，容积150L");
        device10.setUnitPrice(new BigDecimal("8500.00"));
        device10.setQuantity(2);
        device10.setPurchaseDate(LocalDate.now().minusMonths(7));
        device10.setManufacturer("上海一恒");
        device10.setWarrantyUntil(LocalDate.now().plusYears(2));
        device10.setHandlerUserId(teacher1.getId());
        device10.setStatus("IN_STOCK");
        device10.setCreatedAt(LocalDateTime.now());
        device10.setUpdatedAt(LocalDateTime.now());
        device10 = deviceRepository.save(device10);

        // 6. 创建耗材
        Consumable consumable1 = new Consumable();
        consumable1.setName("实验用试剂A");
        consumable1.setCategory("化学试剂");
        consumable1.setSpec("500ml/瓶");
        consumable1.setUnit("瓶");
        consumable1.setUnitPrice(new BigDecimal("50.00"));
        consumable1 = consumableRepository.save(consumable1);

        Consumable consumable2 = new Consumable();
        consumable2.setName("实验用试剂B");
        consumable2.setCategory("化学试剂");
        consumable2.setSpec("250ml/瓶");
        consumable2.setUnit("瓶");
        consumable2.setUnitPrice(new BigDecimal("30.00"));
        consumable2 = consumableRepository.save(consumable2);

        Consumable consumable3 = new Consumable();
        consumable3.setName("一次性实验手套");
        consumable3.setCategory("防护用品");
        consumable3.setSpec("100只/盒");
        consumable3.setUnit("盒");
        consumable3.setUnitPrice(new BigDecimal("25.00"));
        consumable3 = consumableRepository.save(consumable3);

        // 7. 创建库存
        Inventory inv1 = new Inventory();
        inv1.setItemId(consumable1.getId());
        inv1.setLaboratoryId(lab1.getId());
        inv1.setQuantity(20);
        inv1.setSafetyStock(10);
        inventoryRepository.save(inv1);

        Inventory inv2 = new Inventory();
        inv2.setItemId(consumable2.getId());
        inv2.setLaboratoryId(lab1.getId());
        inv2.setQuantity(15);
        inv2.setSafetyStock(5);
        inventoryRepository.save(inv2);

        Inventory inv3 = new Inventory();
        inv3.setItemId(consumable3.getId());
        inv3.setLaboratoryId(lab1.getId());
        inv3.setQuantity(50);
        inv3.setSafetyStock(20);
        inventoryRepository.save(inv3);

        // 8. 创建借还记录（示例）
        BorrowTransaction borrow1 = new BorrowTransaction();
        borrow1.setDeviceId(device1.getId());
        borrow1.setBorrowerUserId(teacher1.getId());
        borrow1.setBorrowedAt(LocalDateTime.now().minusDays(5));
        borrow1.setDueAt(LocalDateTime.now().plusDays(2));
        borrow1.setCheckoutOperatorId(admin.getId());
        borrow1.setStatus("BORROWED");
        borrow1 = borrowRepository.save(borrow1);

        // 更新设备状态
        device1.setStatus("IN_USE");
        device1.setUpdatedAt(LocalDateTime.now());
        deviceRepository.save(device1);

        System.out.println("测试数据初始化完成！");
        System.out.println("默认管理员账号: admin / admin123");
        System.out.println("测试用户: teacher1 / 123456, teacher2 / 123456, teacher3 / 123456, teacher4 / 123456");
        System.out.println("已创建: 4个院系, 4个实验室, 5个用户, 7个设备类别, 10个设备, 3个耗材, 1条借还记录");
    }
}

