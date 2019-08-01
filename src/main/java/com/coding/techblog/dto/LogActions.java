package com.coding.techblog.dto;

public enum LogActions {

    LOGIN("Đăng nhập"), UP_PWD("Thay đổi mật khẩu"), UP_INFO("Thay đổi thông tin"),
    DEL_ARTICLE("Xóa bài viết"), DEL_PAGE("Xóa trang"), SYS_BACKUP("Sao lưu hệ thống"),
    SYS_SETTING("Lưu cài đặt hệ thống"), INIT_SITE("Khởi tạo");

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    LogActions(String action) {
        this.action = action;
    }
}
