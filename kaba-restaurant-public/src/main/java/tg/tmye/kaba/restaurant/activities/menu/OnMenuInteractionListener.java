package tg.tmye.kaba.restaurant.activities.menu;

/**
 * By abiguime on 2020/6/19.
 * email: 2597434002@qq.com
 */
public interface OnMenuInteractionListener {

    void enterSubMenuFoodList(int sub_menu_id);

    void hideSubMenu(int menu_id);

    void deleteSubMenu(int id);
}
