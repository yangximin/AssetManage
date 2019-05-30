package com.yang.assetmanage.entity;


import com.yang.assetmanage.view.CustomTabEntity;

public class TabEntity extends BaseEntity implements CustomTabEntity {
    private String title;
    private int selectedIcon;
    private int unSelectedIcon;

    private boolean isVisible = true;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    public TabEntity(String title, boolean isVisible) {
        this.title = title;
        this.isVisible = isVisible;
    }

    public TabEntity(String title) {
        this.title = title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
