package ng.org.mirabilia.pms.views.forms.location.state;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import ng.org.mirabilia.pms.domain.entities.State;
import ng.org.mirabilia.pms.services.StateService;

import java.util.function.Consumer;

public class AddStateForm extends Dialog {

    private final StateService stateService;
    private final TextField nameField;
    private final TextField stateCodeField;
    private final Consumer<Void> onSuccess;

    public AddStateForm(StateService stateService, Consumer<Void> onSuccess) {
        this.stateService = stateService;
        this.onSuccess = onSuccess;

        this.setModal(true);
        this.setDraggable(false);
        this.setResizable(false);
        this.addClassName("custom-form");

        H2 header = new H2("New State");
        header.addClassName("custom-form-header");

        FormLayout formLayout = new FormLayout();
        nameField = new TextField("Name");
        stateCodeField = new TextField("State Code");

        formLayout.add(nameField, stateCodeField);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

        Button discardButton = new Button("Discard Changes", e -> this.close());
        discardButton.addClassName("custom-button");

        Button saveButton = new Button("Save", e -> saveState());
        saveButton.addClassName("custom-button");

        discardButton.addClassName("custom-button");
        discardButton.addClassName("custom-discard-button");
        saveButton.addClassName("custom-button");
        saveButton.addClassName("custom-save-button");

        discardButton.addClickShortcut(Key.ESCAPE);
        saveButton.addClickShortcut(Key.ENTER);

        HorizontalLayout footer = new HorizontalLayout(discardButton, saveButton);
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        VerticalLayout formContent = new VerticalLayout(header, formLayout, footer);
        formContent.setSpacing(true);
        formContent.setPadding(true);

        add(formContent);
    }

    private void saveState() {
        String name = nameField.getValue();
        String stateCode = stateCodeField.getValue();

        if (name.isEmpty() || stateCode.isEmpty()) {
            Notification.show("Please fill out all fields", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (stateService.stateExists(name, stateCode)) {
            Notification.show("State with this name or code already exists", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        State newState = new State();
        newState.setName(name);
        newState.setStateCode(stateCode);

        stateService.addState(newState);

        Notification notification = Notification.show("State added successfully", 3000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        this.close();
        onSuccess.accept(null);
    }
}
