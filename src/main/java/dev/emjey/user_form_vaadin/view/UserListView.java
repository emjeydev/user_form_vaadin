package dev.emjey.user_form_vaadin.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.emjey.user_form_vaadin.entity.User;
import dev.emjey.user_form_vaadin.service.UserService;
import jakarta.annotation.security.PermitAll;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("User List")
@PermitAll
public class UserListView extends VerticalLayout {

    private final UserService userService;
    private final Grid<User> grid;

    public UserListView(UserService userService) {
        this.userService = userService;
        this.grid = new Grid<>(User.class, false);

        configureGrid();

        Button refreshButton = new Button("Refresh", VaadinIcon.REFRESH.create());
        refreshButton.addClickListener(e -> {
            userService.refreshCache(); // New method to force cache refresh
            refreshGrid();
        });

        add(new H1("Registered Users"), refreshButton, grid);
        setSizeFull();
        refreshGrid();
    }

    private void configureGrid() {
        grid.addColumn(User::getFirstName).setHeader("First Name").setSortable(true);
        grid.addColumn(User::getLastName).setHeader("Last Name").setSortable(true);
        grid.addColumn(User::getUsername).setHeader("Username").setSortable(true);
        grid.addColumn(User::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(user ->
                user.getBirthdate() != null ? user.getBirthdate().toString() : ""
        ).setHeader("Birthdate");

        grid.setWidthFull();
        grid.getStyle().set("max-height", "70vh");
    }

    private void refreshGrid() {
        grid.setItems(userService.getAllUsers());
    }
}