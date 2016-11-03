package beans;

/**
 * Created by Administrator on 2016/11/3.
 */
public class MenuItem {

    // 图标资源id
    public int iconResId;

    // 菜单的标题
    public String text;

    public MenuItem() {

    }

    public MenuItem(String text, int iconResId) {
        this.text = text;
        this.iconResId = iconResId;
    }

}
