package com.ff.shop.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.ff.common.base.BaseModel;

@TableName("tb_reservation_information")
public class ReservationInformation extends BaseModel {
    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @TableField("name_visitor")
    private String nameVisitor;

    @TableField("phone_number")
    private String phoneNumber;

    @TableField("project_name")
    private String projectName;

    @TableField("user_id")
    private Integer userId;

    @TableField("details_cause")
    private String detailsCause;

    private Integer status;

    @TableField("create_time")
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameVisitor() {
        return nameVisitor;
    }

    public void setNameVisitor(String nameVisitor) {
        this.nameVisitor = nameVisitor;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDetailsCause() {
        return detailsCause;
    }

    public void setDetailsCause(String detailsCause) {
        this.detailsCause = detailsCause;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
