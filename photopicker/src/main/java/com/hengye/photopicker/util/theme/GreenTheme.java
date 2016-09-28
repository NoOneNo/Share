package com.hengye.photopicker.util.theme;

import com.hengye.photopicker.R;

public class GreenTheme extends BaseTheme{

    @Override
    public int getTitleBackground() {
        return R.color.pp_theme_green_title_bg;
    }

    @Override
    public int getTitleBackIcon() {
        return R.drawable.btn_back;
    }

    @Override
    public int getTitleTextFontColor() {
        return R.color.pp_theme_green_title_font;
    }

    @Override
    public int getTitleNavigationSelectedIcon() {
        return R.drawable.navigationbar_arrow_white_up;
    }

    @Override
    public int getTitleNavigationUnSelectedIcon() {
        return R.drawable.navigationbar_arrow_white_down;
    }

    @Override
    public int getAlbumListViewSelector(){
        return R.drawable.selector_listview_album_green;
    }

    @Override
    public int getAlbumListItemSelected() {
        return R.color.pp_theme_green_album_list_selected;
    }

    @Override
    public int getAlbumPointSelected() {
        return R.drawable.shape_point_green;
    }

    @Override
    public int getPhotoSelected() {
        return R.drawable.compose_photo_preview_green_right;
    }

    @Override
    public int getNextBtn(){
        return R.drawable.selector_btn_green;
    }
}
