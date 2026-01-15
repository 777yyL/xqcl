-- 需求管理系统数据库表结构
-- Database: PostgreSQL 11

-- 需求列表表（主表）
DROP TABLE IF EXISTS req_list;
CREATE TABLE req_list (
    -- 主键和基本信息
    req_no VARCHAR(100) PRIMARY KEY,
    project_name VARCHAR(200),
    opportunity_no VARCHAR(100),

    -- 行业和区域信息
    industry VARCHAR(100),
    sub_industry VARCHAR(100),
    region VARCHAR(100),
    country VARCHAR(100),

    -- 产品信息
    product_line VARCHAR(100),
    product_series VARCHAR(100),
    product_model VARCHAR(100),
    software_name VARCHAR(100),
    software_version VARCHAR(50),

    -- 需求状态
    status VARCHAR(50),
    is_scheduled VARCHAR(10),
    is_reuse VARCHAR(10),
    urgency VARCHAR(50),
    eval_type VARCHAR(50),

    -- 人员信息
    creator VARCHAR(100),
    creator_dept VARCHAR(200),
    req_owner VARCHAR(100),
    owner_dept VARCHAR(200),
    current_handler VARCHAR(100),

    -- 时间信息
    create_time TIMESTAMP,
    submit_time TIMESTAMP,
    last_submit_time TIMESTAMP,
    eval_time TIMESTAMP,
    last_eval_time TIMESTAMP,
    total_eval_hours DECIMAL(10,2),
    eval_stay_days DECIMAL(10,2),

    -- 排期信息
    schedule_start_time TIMESTAMP,
    schedule_end_time TIMESTAMP,

    -- 工作量信息
    total_workload DECIMAL(10,2),
    dev_hq_workload DECIMAL(10,2),
    dev_region VARCHAR(100),
    dev_region_workload DECIMAL(10,2),
    total_order_workload DECIMAL(10,2),
    order_hq_workload DECIMAL(10,2),
    order_region VARCHAR(100),
    order_region_workload DECIMAL(10,2),
    system_test_workload DECIMAL(10,2),
    integration_test_workload DECIMAL(10,2),
    learning_cost_workload DECIMAL(10,2),
    process_manage_workload DECIMAL(10,2),
    other_workload_detail TEXT,

    -- 其他信息
    expected_complete_time TIMESTAMP,
    custom_no VARCHAR(100),
    jkn_no VARCHAR(100),
    dev_no VARCHAR(100),

    -- 系统字段
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500)
);

-- 表注释
COMMENT ON TABLE req_list IS '需求列表表';

-- 字段注释
COMMENT ON COLUMN req_list.req_no IS '需求评估单号';
COMMENT ON COLUMN req_list.project_name IS '项目名称';
COMMENT ON COLUMN req_list.opportunity_no IS '商机编号';
COMMENT ON COLUMN req_list.industry IS '行业';
COMMENT ON COLUMN req_list.sub_industry IS '子行业';
COMMENT ON COLUMN req_list.region IS '区域';
COMMENT ON COLUMN req_list.country IS '国家或地区';
COMMENT ON COLUMN req_list.product_line IS '产品线';
COMMENT ON COLUMN req_list.product_series IS '产品系列';
COMMENT ON COLUMN req_list.product_model IS '产品型号';
COMMENT ON COLUMN req_list.software_name IS '软件名称';
COMMENT ON COLUMN req_list.software_version IS '软件版本';
COMMENT ON COLUMN req_list.status IS '状态';
COMMENT ON COLUMN req_list.is_scheduled IS '是否排期';
COMMENT ON COLUMN req_list.is_reuse IS '是否复用方案';
COMMENT ON COLUMN req_list.urgency IS '紧急程度';
COMMENT ON COLUMN req_list.eval_type IS '评估类型';
COMMENT ON COLUMN req_list.creator IS '评估单创建人';
COMMENT ON COLUMN req_list.creator_dept IS '创建人部门';
COMMENT ON COLUMN req_list.req_owner IS '需求负责人';
COMMENT ON COLUMN req_list.owner_dept IS '负责人所属部门';
COMMENT ON COLUMN req_list.current_handler IS '当前处理人';
COMMENT ON COLUMN req_list.create_time IS '评估单创建时间';
COMMENT ON COLUMN req_list.submit_time IS '评估单提交时间';
COMMENT ON COLUMN req_list.last_submit_time IS '最后提交时间';
COMMENT ON COLUMN req_list.eval_time IS '评估时间';
COMMENT ON COLUMN req_list.last_eval_time IS '最后评估时间';
COMMENT ON COLUMN req_list.total_eval_hours IS '总评估用时（小时）';
COMMENT ON COLUMN req_list.eval_stay_days IS '评估停留时间（天）';
COMMENT ON COLUMN req_list.schedule_start_time IS '排期开始时间';
COMMENT ON COLUMN req_list.schedule_end_time IS '排期结束时间';
COMMENT ON COLUMN req_list.total_workload IS '总工作量（人天）';
COMMENT ON COLUMN req_list.dev_hq_workload IS '开发资源分布总部工作量(人天)';
COMMENT ON COLUMN req_list.dev_region IS '开发资源分布区域';
COMMENT ON COLUMN req_list.dev_region_workload IS '开发资源分布区域工作量(人天)';
COMMENT ON COLUMN req_list.total_order_workload IS '总订单核算工作量（人天）';
COMMENT ON COLUMN req_list.order_hq_workload IS '订单核算总部工作量(人天)';
COMMENT ON COLUMN req_list.order_region IS '订单核算区域';
COMMENT ON COLUMN req_list.order_region_workload IS '订单核算区域工作量(人天)';
COMMENT ON COLUMN req_list.system_test_workload IS '系统测试工作量（人天）';
COMMENT ON COLUMN req_list.integration_test_workload IS '集成测试工作量（人天）';
COMMENT ON COLUMN req_list.learning_cost_workload IS '学习成本工作量（人天）';
COMMENT ON COLUMN req_list.process_manage_workload IS '流程管理工作量（人天）';
COMMENT ON COLUMN req_list.other_workload_detail IS '其他工作量详情';
COMMENT ON COLUMN req_list.expected_complete_time IS '期望完成时间';
COMMENT ON COLUMN req_list.custom_no IS '定制单号';
COMMENT ON COLUMN req_list.jkn_no IS 'JKN单号';
COMMENT ON COLUMN req_list.dev_no IS '开发单号';
COMMENT ON COLUMN req_list.created_at IS '创建时间';
COMMENT ON COLUMN req_list.updated_at IS '更新时间';
COMMENT ON COLUMN req_list.remark IS '备注';

-- 创建索引
CREATE INDEX idx_req_list_project_name ON req_list(project_name);
CREATE INDEX idx_req_list_status ON req_list(status);
CREATE INDEX idx_req_list_req_owner ON req_list(req_owner);
CREATE INDEX idx_req_list_submit_time ON req_list(submit_time);


-- 需求详情表（从表）
DROP TABLE IF EXISTS req_detail;
CREATE TABLE req_detail (
    -- 主键（使用自增ID）
    id BIGSERIAL PRIMARY KEY,
    -- 联合唯一键：一个需求列表下可以有多个需求详情，通过需求名称区分
    req_no VARCHAR(100),
    req_name VARCHAR(500),

    -- 基本信息
    project_name VARCHAR(200),
    opportunity_no VARCHAR(100),
    industry VARCHAR(100),
    sub_industry VARCHAR(100),
    region VARCHAR(100),

    -- 产品信息
    product_line VARCHAR(100),
    product_series VARCHAR(100),
    product_model VARCHAR(100),
    software_name VARCHAR(100),
    software_version VARCHAR(50),
    status VARCHAR(50),

    -- 需求详情
    req_scene TEXT,
    req_desc TEXT,
    rd_eval TEXT,
    component_id VARCHAR(100),
    component_version VARCHAR(50),
    req_category VARCHAR(100),
    req_tag VARCHAR(200),
    is_reuse VARCHAR(10),

    -- 人员信息
    creator VARCHAR(100),
    req_owner VARCHAR(100),
    owner_dept VARCHAR(200),
    evaluator VARCHAR(100),
    evaluator_dept VARCHAR(200),

    -- 时间信息
    create_time TIMESTAMP,
    submit_time TIMESTAMP,
    complete_time TIMESTAMP,
    component_eval_start_time TIMESTAMP,
    component_eval_end_time TIMESTAMP,
    component_eval_cycle VARCHAR(50),
    eval_hours DECIMAL(10,2),
    stay_time DECIMAL(10,2),

    -- 工作量信息
    eval_workload DECIMAL(10,2),
    workload_detail TEXT,

    -- 排期信息
    rd_schedule_start_time TIMESTAMP,
    rd_schedule_end_time TIMESTAMP,
    schedule_start_time TIMESTAMP,
    schedule_end_time TIMESTAMP,

    -- 其他信息
    custom_no VARCHAR(100),
    dev_no VARCHAR(100),

    -- 系统字段
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500)
);

-- 表注释
COMMENT ON TABLE req_detail IS '需求详情表';

-- 字段注释
COMMENT ON COLUMN req_detail.id IS '主键ID';
COMMENT ON COLUMN req_detail.req_no IS '需求评估单号';
COMMENT ON COLUMN req_detail.req_name IS '需求名称';
COMMENT ON COLUMN req_detail.project_name IS '项目名称';
COMMENT ON COLUMN req_detail.opportunity_no IS '商机编号';
COMMENT ON COLUMN req_detail.industry IS '行业';
COMMENT ON COLUMN req_detail.sub_industry IS '子行业';
COMMENT ON COLUMN req_detail.region IS '区域';
COMMENT ON COLUMN req_detail.product_line IS '产品线';
COMMENT ON COLUMN req_detail.product_series IS '产品系列';
COMMENT ON COLUMN req_detail.product_model IS '产品型号';
COMMENT ON COLUMN req_detail.software_name IS '软件名称';
COMMENT ON COLUMN req_detail.software_version IS '软件版本';
COMMENT ON COLUMN req_detail.status IS '状态';
COMMENT ON COLUMN req_detail.req_scene IS '需求场景';
COMMENT ON COLUMN req_detail.req_desc IS '需求描述';
COMMENT ON COLUMN req_detail.rd_eval IS '研发评估';
COMMENT ON COLUMN req_detail.component_id IS '组件标识';
COMMENT ON COLUMN req_detail.component_version IS '组件版本';
COMMENT ON COLUMN req_detail.req_category IS '需求分类';
COMMENT ON COLUMN req_detail.req_tag IS '需求标签';
COMMENT ON COLUMN req_detail.is_reuse IS '是否复用方案';
COMMENT ON COLUMN req_detail.creator IS '评估单创建人';
COMMENT ON COLUMN req_detail.req_owner IS '需求负责人';
COMMENT ON COLUMN req_detail.owner_dept IS '负责人所属部门';
COMMENT ON COLUMN req_detail.evaluator IS '评估人';
COMMENT ON COLUMN req_detail.evaluator_dept IS '评估人所属部门';
COMMENT ON COLUMN req_detail.create_time IS '评估单创建时间';
COMMENT ON COLUMN req_detail.submit_time IS '评估单提交时间';
COMMENT ON COLUMN req_detail.complete_time IS '评估单完成时间';
COMMENT ON COLUMN req_detail.component_eval_start_time IS '组件评估开始时间';
COMMENT ON COLUMN req_detail.component_eval_end_time IS '组件评估完成时间';
COMMENT ON COLUMN req_detail.component_eval_cycle IS '组件评估周期';
COMMENT ON COLUMN req_detail.eval_hours IS '评估用时(h)';
COMMENT ON COLUMN req_detail.stay_time IS '停留时间';
COMMENT ON COLUMN req_detail.eval_workload IS '评估工作量';
COMMENT ON COLUMN req_detail.workload_detail IS '工作量详情';
COMMENT ON COLUMN req_detail.rd_schedule_start_time IS '排期开始时间（研发评估）';
COMMENT ON COLUMN req_detail.rd_schedule_end_time IS '排期结束时间（研发评估）';
COMMENT ON COLUMN req_detail.schedule_start_time IS '排期开始时间';
COMMENT ON COLUMN req_detail.schedule_end_time IS '排期结束时间';
COMMENT ON COLUMN req_detail.custom_no IS '定制单号';
COMMENT ON COLUMN req_detail.dev_no IS '开发单号';
COMMENT ON COLUMN req_detail.created_at IS '创建时间';
COMMENT ON COLUMN req_detail.updated_at IS '更新时间';
COMMENT ON COLUMN req_detail.remark IS '备注';

-- 创建索引
CREATE INDEX idx_req_detail_req_no ON req_detail(req_no);
CREATE INDEX idx_req_detail_req_name ON req_detail(req_name);
CREATE UNIQUE INDEX idx_req_detail_unique ON req_detail(req_no, req_name);

-- 外键关联（可选，根据需要是否启用）
-- ALTER TABLE req_detail ADD CONSTRAINT fk_req_detail_req_no
--     FOREIGN KEY (req_no) REFERENCES req_list(req_no) ON DELETE CASCADE;
