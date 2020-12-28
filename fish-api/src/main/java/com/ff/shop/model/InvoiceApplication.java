package com.ff.shop.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.ff.common.base.BaseModel;

@TableName("tb_invoice_application")
public class InvoiceApplication extends BaseModel {
    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("unit_name")
    private String unitName;

    @TableField("credit_code")
    private String creditCode;

    @TableField("bank_account")
    private String bankAccount;

    @TableField("opening_bank")
    private String openingBank;

    @TableField("company_address")
    private String companyAddress;

    @TableField("company_telephone")
    private String companyTelephone;

    @TableField("project_leader")
    private String projectLeader;

    @TableField("contract_categories")
    private Integer contractCategories;

    @TableField("item_category")
    private Integer itemCategory;

    @TableField("contract_amount")
    private String contractAmount;

    @TableField("invoice_amount")
    private String invoiceAmount;

    @TableField("payment_date")
    private String paymentDate;

    @TableField("entry_name")
    private String entryName;

    @TableField("project_address")
    private String projectAddress;

    private String others;

    @TableField("add_time")
    private String addTime;

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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyTelephone() {
        return companyTelephone;
    }

    public void setCompanyTelephone(String companyTelephone) {
        this.companyTelephone = companyTelephone;
    }

    public String getProjectLeader() {
        return projectLeader;
    }

    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    public Integer getContractCategories() {
        return contractCategories;
    }

    public void setContractCategories(Integer contractCategories) {
        this.contractCategories = contractCategories;
    }

    public Integer getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(Integer itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(String contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
