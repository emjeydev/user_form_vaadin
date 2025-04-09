package dev.emjey.user_form_vaadin.configuration;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@Theme(themeClass = Material.class)
@PWA(name = "User Form Vaadin", shortName = "UserForm")
public class ApplicationConfig implements AppShellConfigurator {
}