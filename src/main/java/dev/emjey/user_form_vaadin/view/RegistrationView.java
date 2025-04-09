package dev.emjey.user_form_vaadin.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.emjey.user_form_vaadin.entity.User;
import dev.emjey.user_form_vaadin.service.UserService;
import jakarta.annotation.security.PermitAll;

import java.sql.Date;

@Route(value = "register", layout = MainLayout.class)
@JsModule("./js/script.js")
@PageTitle("Registration")
@PermitAll
public class RegistrationView extends VerticalLayout {

    private final UserService userService;

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final TextField username = new TextField("Username");
    private final EmailField email = new EmailField("Email");
    private final DatePicker birthdate = new DatePicker("Birthdate");
    private final Button submit = new Button("Register");

    public RegistrationView(UserService userService) {
        this.userService = userService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstName, lastName,
                username, email,
                birthdate, submit
        );
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );

        submit.addClickListener(e -> registerUser());

        add(new H1("User Registration"), formLayout);
    }

    private void registerUser() {
        try {
            User user = new User();
            user.setFirstName(firstName.getValue());
            user.setLastName(lastName.getValue());
            user.setUsername(username.getValue());
            user.setEmail(email.getValue());
            user.setBirthdate(Date.valueOf(birthdate.getValue()));

            userService.createUser(user);
            Notification.show("User registered successfully!");
            clearForm();
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage(), 3000,
                    Notification.Position.MIDDLE);
        }
    }

    private void clearForm() {
        firstName.clear();
        lastName.clear();
        username.clear();
        email.clear();
        birthdate.clear();
    }
}