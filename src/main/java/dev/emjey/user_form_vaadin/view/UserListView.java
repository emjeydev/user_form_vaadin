package dev.emjey.user_form_vaadin.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.emjey.user_form_vaadin.entity.User;
import dev.emjey.user_form_vaadin.service.UserService;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("User List")
@PermitAll
public class UserListView extends VerticalLayout {

    private final UserService userService;
    private final Grid<User> grid;
    private final List<User> users = new ArrayList<>();

    public UserListView(UserService userService) {
        this.userService = userService;
        this.grid = new Grid<>(User.class, false);

        configureGrid();

        Button refreshButton = new Button("Refresh", VaadinIcon.REFRESH.create());
        refreshButton.addClickListener(e -> refreshGrid());

        add(new H1("Registered Users"), refreshButton, grid);
        setSizeFull();
        refreshGrid();
    }

    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(User::getFirstName).setHeader("First Name").setSortable(true);
        grid.addColumn(User::getLastName).setHeader("Last Name").setSortable(true);
        grid.addColumn(User::getUsername).setHeader("Username").setSortable(true);
        grid.addColumn(User::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(user ->
                user.getBirthdate() != null ? user.getBirthdate().toString() : ""
        ).setHeader("Birthdate");

        grid.addComponentColumn(user -> {
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addClickListener(e -> editUser(user));
            return editButton;
        }).setHeader("Actions").setWidth("100px").setFlexGrow(0);

        grid.setWidthFull();
        grid.getStyle().set("max-height", "70vh");
    }

    private void editUser(User user) {
        Dialog editDialog = new Dialog();
        editDialog.setCloseOnEsc(true);
        editDialog.setCloseOnOutsideClick(false);

        TextField firstName = new TextField("First Name");
        firstName.setValue(user.getFirstName());

        TextField lastName = new TextField("Last Name");
        lastName.setValue(user.getLastName());

        TextField username = new TextField("Username");
        username.setValue(user.getUsername());

        EmailField email = new EmailField("Email");
        email.setValue(user.getEmail());

        DatePicker birthdate = new DatePicker("Birthdate");
        if (user.getBirthdate() != null) {
            birthdate.setValue(user.getBirthdate().toLocalDate());
        }

        Button saveButton = new Button("Save", VaadinIcon.CHECK.create());
        saveButton.addClickListener(e -> {
            try {
                user.setFirstName(firstName.getValue());
                user.setLastName(lastName.getValue());
                user.setUsername(username.getValue());
                user.setEmail(email.getValue());
                if (birthdate.getValue() != null) {
                    user.setBirthdate(java.sql.Date.valueOf(birthdate.getValue()));
                }

                userService.updateUser(user);
                refreshGrid();
                Notification.show("User updated successfully!");
                editDialog.close();
            } catch (Exception ex) {
                Notification.show("Error updating user: " + ex.getMessage(),
                        3000, Notification.Position.MIDDLE);
            }
        });

        Button cancelButton = new Button("Cancel", e -> editDialog.close());

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, username, email, birthdate);

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttons);
        dialogLayout.setPadding(false);

        editDialog.add(dialogLayout);
        editDialog.open();
    }

    private void refreshGrid() {
        userService.refreshCache();
        users.clear();
        users.addAll(userService.getAllUsers());
        grid.setItems(users);
    }
}
