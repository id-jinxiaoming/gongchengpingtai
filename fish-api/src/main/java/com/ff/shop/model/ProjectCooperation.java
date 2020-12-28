package com.ff.shop.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.ff.common.base.BaseModel;

import java.math.BigDecimal;

@TableName("tb_project_cooperation")
public class ProjectCooperation extends BaseModel {

    private static final long serialVersionUID =1L;

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("entry_name")
    private String entryName;

    @TableField("project_cost")
    private BigDecimal projectCost;

    @TableField("item_category")
    private String itemCategory;

    @TableField("item_address")
    private String itemAddress;

    @TableField("cooperation_project")
    private String cooperationProject;

    @TableField("other_instructions")
    private String otherInstructions;

    @TableField("create_time")
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public BigDecimal getProjectCost() {
        return projectCost;
    }

    public void setProjectCost(BigDecimal projectCost) {
        this.projectCost = projectCost;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemAddress() {
        return itemAddress;
    }

    public void setItemAddress(String itemAddress) {
        this.itemAddress = itemAddress;
    }

    public String getCooperationProject() {
        return cooperationProject;
    }

    public void setCooperationProject(String cooperationProject) {
        this.cooperationProject = cooperationProject;
    }

    public String getOtherInstructions() {
        return otherInstructions;
    }

    public void setOtherInstructions(String otherInstructions) {
        this.otherInstructions = otherInstructions;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
