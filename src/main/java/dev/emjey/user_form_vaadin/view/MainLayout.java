package dev.emjey.user_form_vaadin.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("User Management");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        addToNavbar(toggle, title);

        Tabs tabs = new Tabs(
                new Tab(new RouterLink("Registration", RegistrationView.class)),
                new Tab(new RouterLink("User List", UserListView.class))
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        addToDrawer(tabs);
    }
}