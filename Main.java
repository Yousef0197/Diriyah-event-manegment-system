import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Main extends Application {
    private boolean isArabic = false;
    private ObservableList<Event> eventList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private User currentUser = null;
    private final String EVENTS_FILE = "events.txt";
    private final String USERS_FILE = "users.txt";

    @Override
    public void start(Stage primaryStage) {
        loadEvents();
        loadUsers();
        showLoginStage(primaryStage);
    }

    private void loadEvents() {
        eventList.clear();
        File file = new File(EVENTS_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length < 8) continue;
                String title = parts[0];
                String category = parts[1];
                String desc = parts[2];
                String location = parts[3];
                String date = parts[4];
                String img = parts[5];
                double price = Double.parseDouble(parts[6]);
                int tickets = Integer.parseInt(parts[7]);
                eventList.add(new Event(title, category, desc, location, date, img, price, tickets));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveEvents() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EVENTS_FILE))) {
            for (Event ev : eventList) {
                writer.println(
                        ev.getTitle() + "|" +
                        ev.getCategory() + "|" +
                        ev.getDescription() + "|" +
                        ev.getLocation() + "|" +
                        ev.getDate() + "|" +
                        ev.getImagePath() + "|" +
                        ev.getTicketPrice() + "|" +
                        ev.getAvailableTickets()
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadUsers() {
        userList.clear();
        File file = new File(USERS_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length < 5) continue;
                String type = parts[0];
                String username = parts[1];
                String password = parts[2];
                String gender = parts[3];
                int age = Integer.parseInt(parts[4]);
                userList.add(new User(type, username, password, gender, age));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : userList) {
                writer.println(
                        user.getType() + "|" +
                        user.getUsername() + "|" +
                        user.getPassword() + "|" +
                        user.getGender() + "|" +
                        user.getAge()
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private VBox getTopBar() {
        ImageView logo = new ImageView(new Image(new File("diriyahlogo.png").toURI().toString()));
        logo.setFitHeight(90);
        logo.setPreserveRatio(true);
        VBox logoBox = new VBox(logo);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(14, 0, 14, 0));
        return logoBox;
    }

    private void showLoginStage(Stage stage) {
        currentUser = null;
        stage.setTitle(isArabic ? "تسجيل الدخول" : "Login");

        VBox logoBox = getTopBar();
        Label titleLabel = new Label(isArabic ? "الدرعية" : "DIRIYAH");
        titleLabel.getStyleClass().add("header");

        Label userLabel = new Label(isArabic ? "اسم المستخدم:" : "Username:");
        TextField userField = new TextField();
        userField.setPromptText(isArabic ? "ادخل اسم المستخدم" : "Enter username");
        userField.setPrefWidth(320);

        Label passLabel = new Label(isArabic ? "كلمة المرور:" : "Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText(isArabic ? "ادخل كلمة المرور" : "Enter password");
        passField.setPrefWidth(320);

        Button loginBtn = new Button(isArabic ? "دخول" : "Login");
        loginBtn.setPrefWidth(150);
        loginBtn.setStyle("-fx-background-color: #8B5C2A; -fx-text-fill: #fff; -fx-background-radius: 18; -fx-font-size: 18px;");
        Button signupBtn = new Button(isArabic ? "تسجيل جديد" : "Sign Up");
        signupBtn.setPrefWidth(150);
        signupBtn.setStyle("-fx-background-color: #8B5C2A; -fx-text-fill: #fff; -fx-background-radius: 18; -fx-font-size: 18px;");
        Button langBtn = new Button(isArabic ? "EN" : "عربي");
        langBtn.setPrefWidth(100);
        langBtn.setStyle("-fx-background-color: #c9a27c; -fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #543818; -fx-background-radius: 18;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 15px;");

        HBox btnBox = new HBox(18, loginBtn, signupBtn);
        btnBox.setAlignment(Pos.CENTER);

        VBox form = new VBox(12, logoBox, titleLabel, userLabel, userField, passLabel, passField, btnBox, langBtn, errorLabel);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(420);
        form.setPadding(new Insets(30));
        form.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(form);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");

        Scene scene = new Scene(scrollPane, 640, 500);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();
            User found = null;
            for (User u : userList) {
                if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    found = u;
                    break;
                }
            }
            if (found != null) {
                currentUser = found;
                if ("Owner".equals(found.getType()) || "مالك".equals(found.getType())) {
                    showOwnerStage(stage);
                } else {
                    showUserStage(stage);
                }
            } else {
                errorLabel.setText(isArabic ? "بيانات الدخول غير صحيحة." : "Incorrect login credentials.");
            }
        });

        signupBtn.setOnAction(e -> showSignUpStage(stage));
        langBtn.setOnAction(e -> {
            isArabic = !isArabic;
            showLoginStage(stage);
        });
    }

    private void showSignUpStage(Stage stage) {
        stage.setTitle(isArabic ? "تسجيل حساب جديد" : "Sign Up");
        VBox logoBox = getTopBar();

        Label header = new Label(isArabic ? "انشاء حساب جديد" : "Create a New Account");
        header.getStyleClass().add("header");

        Label typeLabel = new Label(isArabic ? "نوع الحساب:" : "Account type:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(isArabic ? "مالك" : "Owner", isArabic ? "مستخدم" : "User");
        typeBox.setPromptText(isArabic ? "اختر النوع" : "Select type");
        typeBox.setPrefWidth(180);

        Label userLabel = new Label(isArabic ? "اسم المستخدم:" : "Username:");
        TextField userField = new TextField();
        userField.setPromptText(isArabic ? "ادخل اسم المستخدم" : "Enter username");
        userField.setPrefWidth(320);

        Label passLabel = new Label(isArabic ? "كلمة المرور:" : "Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText(isArabic ? "ادخل كلمة المرور" : "Enter password");
        passField.setPrefWidth(320);

        Label genderLabel = new Label(isArabic ? "الجنس:" : "Gender:");
        ComboBox<String> genderBox = new ComboBox<>();
        genderBox.getItems().addAll(isArabic ? "ذكر" : "Male", isArabic ? "أنثى" : "Female");
        genderBox.setPromptText(isArabic ? "اختر الجنس" : "Select gender");
        genderBox.setPrefWidth(140);

        Label ageLabel = new Label(isArabic ? "العمر:" : "Age:");
        TextField ageField = new TextField();
        ageField.setPromptText(isArabic ? "ادخل عمرك" : "Enter your age");
        ageField.setPrefWidth(90);

        Button signupBtn = new Button(isArabic ? "تسجيل" : "Sign Up");
        signupBtn.setPrefWidth(130);
        signupBtn.setStyle("-fx-font-size: 17px; -fx-background-radius: 14; -fx-padding: 8 0;");
        Button loginBtn = new Button(isArabic ? "دخول" : "Login");
        loginBtn.setPrefWidth(130);
        loginBtn.setStyle("-fx-font-size: 17px; -fx-background-radius: 14; -fx-padding: 8 0;");
        Button langBtn = new Button(isArabic ? "EN" : "عربي");
        langBtn.setPrefWidth(90);
        langBtn.setStyle("-fx-background-color: #c9a27c; -fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #543818; -fx-background-radius: 18;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #c62828; -fx-font-size: 15px;");

        HBox btnBox = new HBox(16, signupBtn, loginBtn);
        btnBox.setAlignment(Pos.CENTER);

        VBox form = new VBox(10, logoBox, header, typeLabel, typeBox, userLabel, userField, passLabel, passField, genderLabel, genderBox, ageLabel, ageField, btnBox, langBtn, errorLabel);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(420);
        form.setPadding(new Insets(28));
        form.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(form);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");

        Scene scene = new Scene(scrollPane, 640, 540);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

        signupBtn.setOnAction(e -> {
            String typeStr = typeBox.getValue();
            String username = userField.getText().trim();
            String password = passField.getText().trim();
            String genderStr = genderBox.getValue();
            String ageStr = ageField.getText().trim();

            if (typeStr == null || username.isEmpty() || password.isEmpty() || genderStr == null || ageStr.isEmpty()) {
                errorLabel.setText(isArabic ? "يرجى ملء جميع الحقول." : "Please fill all fields.");
                return;
            }
            int age;
            try {
                age = Integer.parseInt(ageStr);
                if (age <= 0) throw new Exception();
            } catch (Exception ex) {
                errorLabel.setText(isArabic ? "العمر يجب أن يكون رقماً صحيحاً." : "Age must be a valid number.");
                return;
            }
            for (User user : userList) {
                if (user.getUsername().equals(username)) {
                    errorLabel.setText(isArabic ? "اسم المستخدم مستخدم بالفعل." : "Username already exists.");
                    return;
                }
            }
            User newUser = new User(typeStr, username, password, genderStr, age);
            userList.add(newUser);
            saveUsers();
            showLoginStage(stage);
        });

        loginBtn.setOnAction(e -> showLoginStage(stage));
        langBtn.setOnAction(e -> {
            isArabic = !isArabic;
            showSignUpStage(stage);
        });
    }

    private TableView<Event> createEventTable() {
        TableView<Event> table = new TableView<>();
        TableColumn<Event, String> titleCol = new TableColumn<>(isArabic ? "العنوان" : "Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setMinWidth(120);

        TableColumn<Event, String> categoryCol = new TableColumn<>(isArabic ? "الفئة" : "Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Event, String> dateCol = new TableColumn<>(isArabic ? "التاريخ" : "Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Event, String> locationCol = new TableColumn<>(isArabic ? "الموقع" : "Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Event, Double> priceCol = new TableColumn<>(isArabic ? "السعر" : "Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("ticketPrice"));

        TableColumn<Event, Integer> ticketsCol = new TableColumn<>(isArabic ? "التذاكر" : "Tickets");
        ticketsCol.setCellValueFactory(new PropertyValueFactory<>("availableTickets"));

        table.getColumns().addAll(titleCol, categoryCol, dateCol, locationCol, priceCol, ticketsCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(260);
        table.setMinHeight(200);

        return table;
    }

    private void showOwnerStage(Stage stage) {
        stage.setTitle(isArabic ? "لوحة المالك" : "Owner Panel");
        VBox logoBox = getTopBar();

        FilteredList<Event> filteredList = new FilteredList<>(eventList, p -> true);
        TableView<Event> table = createEventTable();
        table.setItems(filteredList);

        TextField searchField = new TextField();
        searchField.setPromptText(isArabic ? "بحث..." : "Search...");
        searchField.setPrefWidth(210);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String search = newVal.toLowerCase();
            filteredList.setPredicate(ev ->
                    ev.getTitle().toLowerCase().contains(search) ||
                    ev.getCategory().toLowerCase().contains(search) ||
                    ev.getLocation().toLowerCase().contains(search) ||
                    ev.getDate().toLowerCase().contains(search)
            );
        });

        Button addBtn = new Button(isArabic ? "إضافة فعالية" : "Add Event");
        Button editBtn = new Button(isArabic ? "تعديل المحدد" : "Edit Selected");
        Button delBtn = new Button(isArabic ? "حذف المحدد" : "Delete Selected");
        Button logoutBtn = new Button(isArabic ? "خروج" : "Logout");
        Button langBtn = new Button(isArabic ? "EN" : "عربي");

        addBtn.setStyle("-fx-background-color: #8B5C2A; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 16;");
        editBtn.setStyle("-fx-background-color: #c9a27c; -fx-text-fill: #543818; -fx-font-size: 15px; -fx-background-radius: 16;");
        delBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 16;");
        logoutBtn.setStyle("-fx-background-color: #616161; -fx-text-fill: white; -fx-background-radius: 14;");
        langBtn.setStyle("-fx-background-color: #eee; -fx-text-fill: #543818; -fx-background-radius: 14;");

        HBox btns = new HBox(12, addBtn, editBtn, delBtn, logoutBtn, langBtn);
        btns.setAlignment(Pos.CENTER);

        VBox layout = new VBox(14, logoBox, searchField, table, btns);
        layout.setSpacing(14);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #ecd0b1; -fx-border-radius: 16; -fx-background-radius: 16;");

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");

        Scene scene = new Scene(scrollPane, 780, 520);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        langBtn.setOnAction(e -> { isArabic = !isArabic; showOwnerStage(stage); });
        logoutBtn.setOnAction(e -> showLoginStage(stage));
        addBtn.setOnAction(e -> showEventDialog(null, table));
        editBtn.setOnAction(e -> {
            Event selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showEventDialog(selected, table);
        });
        delBtn.setOnAction(e -> {
            Event selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) { eventList.remove(selected); saveEvents(); }
        });
    }

    private void showUserStage(Stage stage) {
        stage.setTitle(isArabic ? "لوحة المستخدم" : "User Panel");
        VBox logoBox = getTopBar();

        FilteredList<Event> filteredList = new FilteredList<>(eventList, p -> true);
        TableView<Event> table = createEventTable();
        table.setItems(filteredList);
        table.setStyle("-fx-background-color: white; -fx-border-color: #d3b8a0;");

        TextField searchField = new TextField();
        searchField.setPromptText(isArabic ? "بحث..." : "Search...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        VBox categoryBox = new VBox(5);
        Label categoryLabel = new Label(isArabic ? "الفئة:" : "Category:");
        categoryLabel.setStyle("-fx-text-fill: #5d4037; -fx-font-weight: bold;");
        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll(
            isArabic ? "الكل" : "All",
            isArabic ? "ثقافية" : "Cultural",
            isArabic ? "طعام" : "Food",
            isArabic ? "طبيعة" : "Nature",
            isArabic ? "مغامرة" : "Adventure",
            isArabic ? "رياضة" : "Sports"
        );
        categoryFilter.setValue(isArabic ? "الكل" : "All");
        categoryFilter.setStyle("-fx-pref-width: 180; -fx-font-size: 14px;");
        categoryBox.getChildren().addAll(categoryLabel, categoryFilter);
        categoryBox.setAlignment(Pos.CENTER);

        VBox priceBox = new VBox(5);
        Label priceLabel = new Label(isArabic ? "السعر:" : "Price:");
        priceLabel.setStyle("-fx-text-fill: #5d4037; -fx-font-weight: bold;");
        ComboBox<String> priceFilter = new ComboBox<>();
        priceFilter.getItems().addAll(
            isArabic ? "الكل" : "All",
            isArabic ? "منخفض (<50)" : "Low (<50)",
            isArabic ? "متوسط (50-150)" : "Medium (50-150)",
            isArabic ? "مرتفع (>150)" : "High (>150)"
        );
        priceFilter.setValue(isArabic ? "الكل" : "All");
        priceFilter.setStyle("-fx-pref-width: 180; -fx-font-size: 14px;");
        priceBox.getChildren().addAll(priceLabel, priceFilter);
        priceBox.setAlignment(Pos.CENTER);

        HBox filtersBox = new HBox(20, categoryBox, priceBox);
        filtersBox.setAlignment(Pos.CENTER);
        filtersBox.setPadding(new Insets(10));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> 
            updateFilters(filteredList, searchField.getText(), categoryFilter.getValue(), priceFilter.getValue()));
        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> 
            updateFilters(filteredList, searchField.getText(), newVal, priceFilter.getValue()));
        priceFilter.valueProperty().addListener((obs, oldVal, newVal) -> 
            updateFilters(filteredList, searchField.getText(), categoryFilter.getValue(), newVal));

        Button detailsBtn = new Button(isArabic ? "تفاصيل/حجز" : "Details/Book");
        Button logoutBtn = new Button(isArabic ? "خروج" : "Logout");
        Button langBtn = new Button(isArabic ? "EN" : "عربي");

        String buttonStyle = "-fx-background-radius: 15; -fx-padding: 8 20; -fx-font-size: 14px;";
        detailsBtn.setStyle("-fx-background-color: #8B5C2A; -fx-text-fill: white; " + buttonStyle);
        logoutBtn.setStyle("-fx-background-color: #616161; -fx-text-fill: white; " + buttonStyle);
        langBtn.setStyle("-fx-background-color: #d3b8a0; -fx-text-fill: #5d4037; " + buttonStyle);

        HBox btns = new HBox(15, detailsBtn, logoutBtn, langBtn);
        btns.setAlignment(Pos.CENTER);
        btns.setPadding(new Insets(15));

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5e9dc;");

        VBox searchSection = new VBox(15);
        searchSection.setAlignment(Pos.CENTER);
        searchSection.setPadding(new Insets(15));
        searchSection.setStyle("-fx-background-color: #e6d5c3; -fx-background-radius: 10;");
        searchSection.getChildren().addAll(searchField, filtersBox);

        layout.getChildren().addAll(logoBox, searchSection, table, btns);
        VBox.setVgrow(table, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");

        Scene scene = new Scene(scrollPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        detailsBtn.setOnAction(e -> {
            Event selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showEventDetails(selected, table);
        });
        logoutBtn.setOnAction(e -> showLoginStage(stage));
        langBtn.setOnAction(e -> {
            isArabic = !isArabic;
            showUserStage(stage);
        });
    }

private void showEventDialog(Event event, TableView<Event> table) {
    Stage dialog = new Stage();
    dialog.setTitle(event == null
        ? (isArabic ? "إضافة فعالية" : "Add Event")
        : (isArabic ? "تعديل فعالية" : "Edit Event"));

    // إنشاء شبكة لتنسيق الحقول
    GridPane grid = new GridPane();
    grid.setHgap(15);
    grid.setVgap(10);
    grid.setPadding(new Insets(20));
    grid.setAlignment(Pos.CENTER);

    // حقل العنوان
    Label titleLabel = new Label(isArabic ? "عنوان الفعالية:" : "Event Title:");
    titleLabel.getStyleClass().add("form-label");
    TextField titleField = new TextField(event != null ? event.getTitle() : "");
    titleField.setPromptText(isArabic ? "أدخل عنوان الفعالية" : "Enter event title");
    grid.add(titleLabel, 0, 0);
    grid.add(titleField, 1, 0, 2, 1);

    // حقل الفئة
    Label catLabel = new Label(isArabic ? "الفئة:" : "Category:");
    catLabel.getStyleClass().add("form-label");
    ComboBox<String> catBox = new ComboBox<>();
    catBox.getItems().addAll(
        isArabic ? "ثقافية" : "Cultural",
        isArabic ? "طعام"    : "Food",
        isArabic ? "طبيعة"  : "Nature",
        isArabic ? "مغامرة" : "Adventure",
        isArabic ? "رياضة"  : "Sports"
    );
    catBox.setValue(event != null ? event.getCategory() : null);
    catBox.setPromptText(isArabic ? "اختر الفئة" : "Select category");
    grid.add(catLabel, 0, 1);
    grid.add(catBox, 1, 1, 2, 1);

    // حقل عنوان الوصف
    Label descTitleLabel = new Label(isArabic ? "عنوان الوصف:" : "Description Title:");
    descTitleLabel.getStyleClass().add("form-label");
    TextField descTitleField = new TextField(event != null ? event.getDescriptionTitle() : "");
    descTitleField.setPromptText(isArabic ? "أدخل عنوان الوصف" : "Enter description title");
    grid.add(descTitleLabel, 0, 2);
    grid.add(descTitleField, 1, 2, 2, 1);

    // حقل الوصف
    Label descLabel = new Label(isArabic ? "الوصف:" : "Description:");
    descLabel.getStyleClass().add("form-label");
    TextArea descField = new TextArea(event != null ? event.getDescription() : "");
    descField.setPromptText(isArabic ? "أدخل وصف الفعالية" : "Enter event description");
    descField.setPrefRowCount(3);
    grid.add(descLabel, 0, 3);
    grid.add(descField, 1, 3, 2, 1);

    // حقل الموقع
    Label locLabel = new Label(isArabic ? "الموقع:" : "Location:");
    locLabel.getStyleClass().add("form-label");
    TextField locField = new TextField(event != null ? event.getLocation() : "");
    locField.setPromptText(isArabic ? "أدخل موقع الفعالية" : "Enter event location");
    grid.add(locLabel, 0, 4);
    grid.add(locField, 1, 4, 2, 1);

    // حقل التاريخ
    Label dateLabel = new Label(isArabic ? "التاريخ:" : "Date:");
    dateLabel.getStyleClass().add("form-label");
    TextField dateField = new TextField(event != null ? event.getDate() : "");
    dateField.setPromptText(isArabic ? "يوم/شهر/سنة" : "DD/MM/YYYY");
    grid.add(dateLabel, 0, 5);
    grid.add(dateField, 1, 5);

    // حقل الصورة
    Label imgLabel = new Label(isArabic ? "صورة الفعالية:" : "Event Image:");
    imgLabel.getStyleClass().add("form-label");
    TextField imgField = new TextField(event != null ? event.getImagePath() : "");
    imgField.setPromptText(isArabic ? "مسار الصورة" : "Image path");
    Button chooseImg = new Button(isArabic ? "اختيار صورة" : "Choose Image");
    chooseImg.setOnAction(e -> {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fc.showOpenDialog(dialog);
        if (file != null) imgField.setText(file.getAbsolutePath());
    });
    grid.add(imgLabel, 0, 6);
    grid.add(imgField, 1, 6);
    grid.add(chooseImg, 2, 6);

    // حقل السعر
    Label priceLabel = new Label(isArabic ? "سعر التذكرة:" : "Ticket Price:");
    priceLabel.getStyleClass().add("form-label");
    TextField priceField = new TextField(
        event != null ? String.valueOf(event.getTicketPrice()) : ""
    );
    priceField.setPromptText(isArabic ? "بالريال السعودي" : "In SAR");
    grid.add(priceLabel, 0, 7);
    grid.add(priceField, 1, 7);

    // حقل التذاكر
    Label availLabel = new Label(isArabic ? "عدد التذاكر:" : "Available Tickets:");
    availLabel.getStyleClass().add("form-label");
    TextField availField = new TextField(
        event != null ? String.valueOf(event.getAvailableTickets()) : ""
    );
    availField.setPromptText(isArabic ? "الكمية المتاحة" : "Available quantity");
    grid.add(availLabel, 0, 8);
    grid.add(availField, 1, 8);

    // أزرار الحفظ والإلغاء
    Button saveBtn   = new Button(isArabic ? "حفظ"   : "Save");
    Button cancelBtn = new Button(isArabic ? "إلغاء" : "Cancel");
    HBox buttonsBox = new HBox(15, saveBtn, cancelBtn);
    buttonsBox.setAlignment(Pos.CENTER);
    grid.add(buttonsBox, 0, 9, 3, 1);

    // إضافة ScrollPane
    ScrollPane scrollPane = new ScrollPane(grid);
    scrollPane.setFitToWidth(true);
    scrollPane.setStyle("-fx-background: transparent;");

    Scene scene = new Scene(scrollPane, 500, 550);
    // لا تضيف stylesheet هنا حتى لا تُطبَّق قواعد CSS التي قد تخفي العناوين
    dialog.setScene(scene);

    // أحداث الأزرار
    saveBtn.setOnAction(e -> {
        try {
            String title     = titleField.getText().trim();
            String cat       = catBox.getValue();
            String descTitle = descTitleField.getText().trim();
            String desc      = descField.getText().trim();
            String loc       = locField.getText().trim();
            String date      = dateField.getText().trim();
            String img       = imgField.getText().trim();
            double price     = Double.parseDouble(priceField.getText().trim());
            int avail        = Integer.parseInt(availField.getText().trim());

            if (title.isEmpty() || cat == null || descTitle.isEmpty() ||
                desc.isEmpty()  || loc.isEmpty() || date.isEmpty() ||
                price < 0      || avail < 0) {
                throw new Exception();
            }

            if (event == null) {
                eventList.add(new Event(
                    title, cat, descTitle, desc, loc, date, img, price, avail
                ));
            } else {
                event.setTitle(title);
                event.setCategory(cat);
                event.setDescriptionTitle(descTitle);
                event.setDescription(desc);
                event.setLocation(loc);
                event.setDate(date);
                event.setImagePath(img);
                event.setTicketPrice(price);
                event.setAvailableTickets(avail);
                table.refresh();
            }
            saveEvents();
            dialog.close();
        } catch (Exception ex) {
            showAlert(
                isArabic
                  ? "يرجى إدخال بيانات صحيحة في جميع الحقول"
                  : "Please enter valid data in all fields"
            );
        }
    });

    cancelBtn.setOnAction(e -> dialog.close());
    dialog.show();
}




    private void showEventDetails(Event event, TableView<Event> table) {
    Stage detailStage = new Stage();
    detailStage.setTitle(isArabic ? "تفاصيل الفعالية" : "Event Details");

    // العنصر الرئيسي
    VBox mainContainer = new VBox(20);
    mainContainer.setAlignment(Pos.TOP_CENTER);
    mainContainer.setPadding(new Insets(20));
    mainContainer.setStyle("-fx-background-color: #f5e9dc;");

    // شعار التطبيق
    ImageView logo = new ImageView(new Image(new File("diriyahlogo.png").toURI().toString()));
    logo.setFitHeight(70);
    logo.setPreserveRatio(true);
    logo.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");

    // قسم تفاصيل الفعالية
    VBox eventDetails = new VBox(15);
    eventDetails.setPadding(new Insets(20));
    eventDetails.getStyleClass().add("event-detail-container");

    Label titleLabel = new Label(isArabic ? "العنوان:" : "Title:");
    Label titleValue = new Label(event.getTitle());
    
    Label categoryLabel = new Label(isArabic ? "الفئة:" : "Category:");
    Label categoryValue = new Label(event.getCategory());
    
    Label dateLabel = new Label(isArabic ? "التاريخ:" : "Date:");
    Label dateValue = new Label(event.getDate());
    
    Label locationLabel = new Label(isArabic ? "الموقع:" : "Location:");
    Label locationValue = new Label(event.getLocation());
    
    Label priceLabel = new Label(isArabic ? "سعر التذكرة:" : "Ticket Price:");
    Label priceValue = new Label(String.format("%.2f", event.getTicketPrice()) + (isArabic ? " ر.س" : " SAR"));
    
    Label ticketsLabel = new Label(isArabic ? "التذاكر المتاحة:" : "Available Tickets:");
    Label ticketsValue = new Label(String.valueOf(event.getAvailableTickets()));
    
    Label descLabel = new Label(isArabic ? "الوصف:" : "Description:");
    Label descValue = new Label(event.getDescription());
    descValue.setWrapText(true);
    descValue.setMaxWidth(350);

    // تطبيق التنسيقات
    titleLabel.getStyleClass().add("detail-label");
    titleValue.getStyleClass().add("detail-value");
    categoryLabel.getStyleClass().add("detail-label");
    categoryValue.getStyleClass().add("detail-value");
    dateLabel.getStyleClass().add("detail-label");
    dateValue.getStyleClass().add("detail-value");
    locationLabel.getStyleClass().add("detail-label");
    locationValue.getStyleClass().add("detail-value");
    priceLabel.getStyleClass().add("detail-label");
    priceValue.getStyleClass().add("detail-value");
    ticketsLabel.getStyleClass().add("detail-label");
    ticketsValue.getStyleClass().add("detail-value");
    descLabel.getStyleClass().add("detail-label");
    descValue.getStyleClass().add("detail-value");

    // زر الحجز
    Button bookBtn = new Button(isArabic ? "حجز تذكرة" : "Book Ticket");
    bookBtn.getStyleClass().add("book-button");
    bookBtn.setMaxWidth(Double.MAX_VALUE);

    // قسم التذكرة
    VBox ticketSection = new VBox(15);
    ticketSection.getStyleClass().add("ticket-section");
    ticketSection.setVisible(false);

    Label ticketTitle = new Label();
    ticketTitle.getStyleClass().add("ticket-title");

    Label eventNameLabel = new Label(isArabic ? "الفعالية:" : "Event:");
    Label eventNameValue = new Label(event.getTitle());
    
    Label ticketDateLabel = new Label(isArabic ? "التاريخ:" : "Date:");
    Label ticketDateValue = new Label(event.getDate());
    
    Label ticketLocationLabel = new Label(isArabic ? "الموقع:" : "Location:");
    Label ticketLocationValue = new Label(event.getLocation());
    
    Label ticketNumberLabel = new Label(isArabic ? "رقم التذكرة:" : "Ticket Number:");
    Label ticketNumberValue = new Label("#" + (10000 + (int)(Math.random() * 90000)));
    
    // باركود مزيف
    HBox barcodeBox = new HBox(2);
    barcodeBox.setAlignment(Pos.CENTER);
    for (int i = 0; i < 20; i++) {
        Rectangle bar = new Rectangle(8, 40 + (int)(Math.random() * 20));
        bar.setFill(Color.BLACK);
        bar.setArcWidth(3);
        bar.setArcHeight(3);
        barcodeBox.getChildren().add(bar);
    }

    // تنسيق تذكرة
    eventNameLabel.getStyleClass().add("ticket-label");
    eventNameValue.getStyleClass().add("ticket-value");
    ticketDateLabel.getStyleClass().add("ticket-label");
    ticketDateValue.getStyleClass().add("ticket-value");
    ticketLocationLabel.getStyleClass().add("ticket-label");
    ticketLocationValue.getStyleClass().add("ticket-value");
    ticketNumberLabel.getStyleClass().add("ticket-label");
    ticketNumberValue.getStyleClass().add("ticket-value");

    // إضافة عناصر التذكرة
    ticketSection.getChildren().addAll(
        ticketTitle,
        new Separator(),
        new HBox(10, eventNameLabel, eventNameValue),
        new HBox(10, ticketDateLabel, ticketDateValue),
        new HBox(10, ticketLocationLabel, ticketLocationValue),
        new HBox(10, ticketNumberLabel, ticketNumberValue),
        new Label(isArabic ? "باركود التذكرة:" : "Ticket Barcode:"),
        barcodeBox
    );

    // حدث زر الحجز
    bookBtn.setOnAction(e -> {
        if (event.getAvailableTickets() > 0) {
            event.setAvailableTickets(event.getAvailableTickets() - 1);
            saveEvents();
            table.refresh();
            ticketsValue.setText(String.valueOf(event.getAvailableTickets()));
            
            ticketTitle.setText(isArabic ? "تذكرتك لحضور الفعالية" : "Your Event Ticket");
            ticketSection.setVisible(true);
            bookBtn.setDisable(true);
            bookBtn.setText(isArabic ? "تم الحجز" : "Booked");
        }
    });

    // إضافة العناصر إلى الواجهة
    eventDetails.getChildren().addAll(
        titleLabel, titleValue,
        categoryLabel, categoryValue,
        dateLabel, dateValue,
        locationLabel, locationValue,
        priceLabel, priceValue,
        ticketsLabel, ticketsValue,
        descLabel, descValue,
        bookBtn
    );

    mainContainer.getChildren().addAll(logo, eventDetails, ticketSection);

    // إضافة ScrollPane
    ScrollPane scrollPane = new ScrollPane(mainContainer);
    scrollPane.setFitToWidth(true);
    scrollPane.setStyle("-fx-background: transparent;");

    Scene scene = new Scene(scrollPane, 450, 650);
    scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    detailStage.setScene(scene);
    detailStage.show();
}
    private void updateFilters(FilteredList<Event> filteredList, String searchText, String category, String priceRange) {
        filteredList.setPredicate(event -> {
            boolean matchesSearch = searchText.isEmpty() ||
                event.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                event.getCategory().toLowerCase().contains(searchText.toLowerCase()) ||
                event.getLocation().toLowerCase().contains(searchText.toLowerCase());

            boolean matchesCategory = category.equals(isArabic ? "الكل" : "All") || 
                event.getCategory().equals(category);

            boolean matchesPrice = priceRange.equals(isArabic ? "الكل" : "All") ||
                (priceRange.equals(isArabic ? "منخفض (<50)" : "Low (<50)") && event.getTicketPrice() < 50) ||
                (priceRange.equals(isArabic ? "متوسط (50-150)" : "Medium (50-150)") && 
                 event.getTicketPrice() >= 50 && event.getTicketPrice() <= 150) ||
                (priceRange.equals(isArabic ? "مرتفع (>150)" : "High (>150)") && event.getTicketPrice() > 150);

            return matchesSearch && matchesCategory && matchesPrice;
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}