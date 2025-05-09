package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import models.*;
import service.*;
import CustomDataStructures.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.lang.StringBuilder;
import javax.swing.table.TableCellRenderer;

public class MainWindow extends JFrame {
    private RoomService roomService;
    private GuestService guestService;
    private BookingService bookingService;
    private RoomCategory roomCategory;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Guest loggedInGuest = null;

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Vibrant Blue
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);  // Vibrant Green
    private static final Color ACCENT_COLOR = new Color(155, 89, 182);     // Vibrant Purple
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80);         // Dark Blue-Gray

    public MainWindow() {
        initializeServices();
        setupWindow();
        createMainPanel();
        addMenuBar();
        // Show login screen first
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void initializeServices() {
        roomService = new RoomService();
        guestService = new GuestService();
        
        // Initialize rooms
        roomService.addRoom(new Room(101, "Sea View"));
        roomService.addRoom(new Room(102, "Sea View"));
        roomService.addRoom(new Room(103, "Sea View"));
        roomService.addRoom(new Room(104, "Deluxe"));
        roomService.addRoom(new Room(105, "Deluxe"));
        roomService.addRoom(new Room(106, "Deluxe"));
        roomService.addRoom(new Room(107, "Standard"));
        roomService.addRoom(new Room(108, "Standard"));
        roomService.addRoom(new Room(109, "Standard"));

        // Initialize room categories
        roomCategory = new RoomCategory();
        initializeRoomCategories();

        // Initialize booking service with actual rooms
        List<Room> actualRooms = roomService.displayAllRooms();
        bookingService = new BookingService(actualRooms);
    }

    private void initializeRoomCategories() {
        roomCategory.addCategory("Rooms", "Sea View", 6, 5000);
        roomCategory.addCategory("Rooms", "Deluxe", 6, 3500);
        roomCategory.addCategory("Rooms", "Standard", 6, 2500);

        roomCategory.addCategory("Sea View", "101", 2, 5000);
        roomCategory.addCategory("Sea View", "102", 2, 5000);
        roomCategory.addCategory("Sea View", "103", 2, 5000);

        roomCategory.addCategory("Deluxe", "104", 2, 3500);
        roomCategory.addCategory("Deluxe", "105", 2, 3500);
        roomCategory.addCategory("Deluxe", "106", 2, 3500);

        roomCategory.addCategory("Standard", "107", 2, 2500);
        roomCategory.addCategory("Standard", "108", 2, 2500);
        roomCategory.addCategory("Standard", "109", 2, 2500);
    }

    private void setupWindow() {
        setTitle("Invenzo Hotel Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Add different panels
        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createWelcomePanel(), "WELCOME");
        mainPanel.add(createRoomManagementPanel(), "ROOMS");
        mainPanel.add(createBookingPanel(), "BOOKING");
        mainPanel.add(createGuestPanel(), "GUESTS");
        mainPanel.add(createViewBookingsPanel(), "VIEW_BOOKINGS");

        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Guest Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Guest ID:"), gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            String name = nameField.getText().trim();
            if (idText.isEmpty() || name.isEmpty()) {
                showError("Please enter both Guest ID and Name");
                return;
            }
            try {
                int id = Integer.parseInt(idText);
                Guest guest = guestService.searchGuestById(id);
                if (guest != null && guest.getName().equalsIgnoreCase(name)) {
                    loggedInGuest = guest;
                    showSuccess("Login successful! Welcome, " + guest.getName());
                    cardLayout.show(mainPanel, "WELCOME");
                } else {
                    showError("Invalid Guest ID or Name");
                }
            } catch (NumberFormatException ex) {
                showError("Guest ID must be a number");
            }
        });
        return panel;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Welcome message with modern styling
        JLabel welcomeLabel = new JLabel("Welcome to Invenzo", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Quick access buttons with modern design
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton roomsButton = createMenuButton("Room Management", "ROOMS");
        JButton bookingButton = createMenuButton("Book a Room", "BOOKING");
        JButton guestsButton = createMenuButton("Guest Management", "GUESTS");
        JButton viewBookingsButton = createMenuButton("View Bookings", "VIEW_BOOKINGS");
        JButton exitButton = createMenuButton("Exit", "EXIT");

        buttonPanel.add(roomsButton);
        buttonPanel.add(bookingButton);
        buttonPanel.add(guestsButton);
        buttonPanel.add(viewBookingsButton);
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createMenuButton(String text, String action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 100));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        button.addActionListener(e -> {
            if (action.equals("EXIT")) {
                System.exit(0);
            } else {
                cardLayout.show(mainPanel, action);
            }
        });
        return button;
    }

    private JPanel createRoomManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Room Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Date selection panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        datePanel.setBackground(BACKGROUND_COLOR);
        datePanel.add(new JLabel("Check-in Date:"));
        JSpinner checkInSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd");
        checkInSpinner.setEditor(checkInEditor);
        datePanel.add(checkInSpinner);
        datePanel.add(new JLabel("Check-out Date:"));
        JSpinner checkOutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd");
        checkOutSpinner.setEditor(checkOutEditor);
        datePanel.add(checkOutSpinner);
        JButton filterButton = new JButton("Show Availability");
        filterButton.setBackground(PRIMARY_COLOR);
        filterButton.setForeground(Color.WHITE);
        filterButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        datePanel.add(filterButton);
        panel.add(datePanel, BorderLayout.BEFORE_FIRST_LINE);

        // Room list
        JPanel roomListPanel = new JPanel(new BorderLayout());
        roomListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JTable roomTable = createRoomTableForDates(null, null);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        roomListPanel.add(scrollPane, BorderLayout.CENTER);

        // Add refresh button
        JButton refreshButton = new JButton("Refresh Room Status");
        refreshButton.setBackground(PRIMARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.addActionListener(e -> {
            JTable newTable = createRoomTableForDates(null, null);
            scrollPane.setViewportView(newTable);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(refreshButton);
        
        panel.add(roomListPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Filter button action
        filterButton.addActionListener(e -> {
            LocalDate checkIn = ((java.util.Date) checkInSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = ((java.util.Date) checkOutSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            JTable filteredTable = createRoomTableForDates(checkIn, checkOut);
            scrollPane.setViewportView(filteredTable);
        });

        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setBackground(PRIMARY_COLOR);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JTable createRoomTableForDates(LocalDate checkIn, LocalDate checkOut) {
        String[] columnNames = {"Room Number", "Category", "Status", "Booked Dates", "Price"};
        List<Room> rooms = roomService.displayAllRooms();
        Object[][] data = new Object[rooms.size()][5];

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            data[i][0] = room.getRoomNumber();
            data[i][1] = room.getCategory();
            data[i][4] = getRoomPrice(room.getCategory());
            
            // Get booking information for this room
            List<Booking> roomBookings = new ArrayList<>();
            List<Booking> overlappingBookings = new ArrayList<>();
            for (Booking booking : bookingService.getActiveBookings()) {
                if (booking.getRoom().equals(room)) {
                    roomBookings.add(booking);
                    if (checkIn != null && checkOut != null) {
                        // Check for overlap
                        if (!(checkOut.isBefore(booking.getCheckInDate()) || checkIn.isAfter(booking.getCheckOutDate()))) {
                            overlappingBookings.add(booking);
                        }
                    }
                }
            }
            if (checkIn == null || checkOut == null) {
                // Show all bookings if no filter
                if (roomBookings.isEmpty()) {
                    data[i][2] = "Available";
                    data[i][3] = "No bookings";
                } else {
                    StringBuilder bookingInfo = new StringBuilder();
                    for (Booking booking : roomBookings) {
                        if (bookingInfo.length() > 0) bookingInfo.append("\n");
                        bookingInfo.append(String.format("%s to %s (%s)",
                            booking.getCheckInDate(),
                            booking.getCheckOutDate(),
                            booking.getGuest().getName()));
                    }
                    data[i][2] = "Not Available";
                    data[i][3] = bookingInfo.toString();
                }
            } else {
                // Show only overlapping bookings for selected dates
                if (overlappingBookings.isEmpty()) {
                    data[i][2] = "Available";
                    data[i][3] = "No bookings for selected dates";
                } else {
                    StringBuilder bookingInfo = new StringBuilder();
                    for (Booking booking : overlappingBookings) {
                        if (bookingInfo.length() > 0) bookingInfo.append("\n");
                        bookingInfo.append(String.format("%s to %s (%s)",
                            booking.getCheckInDate(),
                            booking.getCheckOutDate(),
                            booking.getGuest().getName()));
                    }
                    data[i][2] = "Not Available";
                    data[i][3] = bookingInfo.toString();
                }
            }
        }

        JTable table = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    String status = (String) getValueAt(row, 2);
                    if (status.equals("Not Available")) {
                        comp.setBackground(new Color(255, 200, 200)); // Light red for booked rooms
                    } else {
                        comp.setBackground(new Color(200, 255, 200)); // Light green for available rooms
                    }
                }
                return comp;
            }
        };
        
        // Set row height to accommodate multiple lines
        table.setRowHeight(60);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Room Number
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Category
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(3).setPreferredWidth(300); // Booked Dates
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Price
        
        return table;
    }

    private double getRoomPrice(String category) {
        switch (category.toLowerCase()) {
            case "sea view": return 5000.0;
            case "deluxe": return 3500.0;
            case "standard": return 2500.0;
            default: return 0.0;
        }
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Book a Room", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Main content panel with card layout for steps
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Step 1: Date Selection Panel
        JPanel dateSelectionPanel = new JPanel(new GridBagLayout());
        dateSelectionPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Check-in date
        gbc.gridx = 0; gbc.gridy = 0;
        dateSelectionPanel.add(new JLabel("Check-in Date:"), gbc);
        gbc.gridx = 1;
        JSpinner checkInSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd");
        checkInSpinner.setEditor(checkInEditor);
        dateSelectionPanel.add(checkInSpinner, gbc);

        // Check-out date
        gbc.gridx = 0; gbc.gridy = 1;
        dateSelectionPanel.add(new JLabel("Check-out Date:"), gbc);
        gbc.gridx = 1;
        JSpinner checkOutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd");
        checkOutSpinner.setEditor(checkOutEditor);
        dateSelectionPanel.add(checkOutSpinner, gbc);

        // Next button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton nextButton = new JButton("Check Available Rooms");
        nextButton.setBackground(PRIMARY_COLOR);
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateSelectionPanel.add(nextButton, gbc);

        // Step 2: Room Selection Panel
        JPanel roomSelectionPanel = new JPanel(new BorderLayout());
        roomSelectionPanel.setBackground(BACKGROUND_COLOR);

        // Available rooms table
        JTable availableRoomsTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(availableRoomsTable);
        roomSelectionPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Guest information panel
        JPanel guestInfoPanel = new JPanel(new GridBagLayout());
        guestInfoPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(10, 10, 10, 10);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        // Guest name
        gbc2.gridx = 0; gbc2.gridy = 0;
        guestInfoPanel.add(new JLabel("Guest Name:"), gbc2);
        gbc2.gridx = 1;
        JTextField guestNameField = new JTextField(20);
        guestInfoPanel.add(guestNameField, gbc2);

        // Book button
        gbc2.gridx = 0; gbc2.gridy = 1;
        gbc2.gridwidth = 2;
        JButton bookButton = new JButton("Book Selected Room");
        bookButton.setBackground(PRIMARY_COLOR);
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        guestInfoPanel.add(bookButton, gbc2);

        roomSelectionPanel.add(guestInfoPanel, BorderLayout.SOUTH);

        // Add panels to content panel
        contentPanel.add(dateSelectionPanel, "DATES");
        contentPanel.add(roomSelectionPanel, "ROOMS");

        // Next button action
        nextButton.addActionListener(e -> {
            LocalDate checkIn = ((java.util.Date) checkInSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = ((java.util.Date) checkOutSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            if (checkIn.isAfter(checkOut)) {
                showError("Check-out date must be after check-in date");
                return;
            }

            // Create table model for available rooms
            String[] columnNames = {"Room Number", "Category", "Price"};
            List<Room> availableRooms = new ArrayList<>();
            for (Room room : roomService.displayAllRooms()) {
                if (bookingService.isRoomAvailable(room, checkIn, checkOut)) {
                    availableRooms.add(room);
                }
            }

            Object[][] data = new Object[availableRooms.size()][3];
            for (int i = 0; i < availableRooms.size(); i++) {
                Room room = availableRooms.get(i);
                data[i][0] = room.getRoomNumber();
                data[i][1] = room.getCategory();
                data[i][2] = getRoomPrice(room.getCategory());
            }

            availableRoomsTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
            availableRoomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Show room selection panel
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "ROOMS");
        });

        // Book button action
        bookButton.addActionListener(e -> {
            int selectedRow = availableRoomsTable.getSelectedRow();
            if (selectedRow == -1) {
                showError("Please select a room");
                return;
            }

            String guestName = guestNameField.getText().trim();
            if (guestName.isEmpty()) {
                showError("Please enter guest name");
                return;
            }

            Guest guest = guestService.searchGuestByName(guestName);
            if (guest == null) {
                showError("Guest not found. Please register first.");
                return;
            }

            int roomNumber = (int) availableRoomsTable.getValueAt(selectedRow, 0);
            Room selectedRoom = roomService.findRoomByNumber(roomNumber);
            LocalDate checkIn = ((java.util.Date) checkInSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = ((java.util.Date) checkOutSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            Booking booking = bookingService.createBooking(guest, selectedRoom, checkIn, checkOut);
            if (booking != null) {
                if (booking.isWaiting()) {
                    showSuccess("Room is not available for selected dates. Added to waiting list!");
                } else {
                    showSuccess("Room booked successfully! Room status will now update.");
                }
                // Automatically refresh Room Management panel
                JPanel roomPanel = createRoomManagementPanel();
                mainPanel.add(roomPanel, "ROOMS");
                cardLayout.show(mainPanel, "ROOMS");
            } else {
                showError("Booking failed. Please try again.");
            }
        });

        panel.add(contentPanel, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setBackground(PRIMARY_COLOR);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "DATES");
            cardLayout.show(mainPanel, "WELCOME");
        });
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGuestPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));

        // Title with icon
        JLabel titleLabel = new JLabel("👥 Guest Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Guest registration form with modern design
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Style for form fields
        Dimension fieldSize = new Dimension(300, 35);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Guest name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(fieldSize);
        nameField.setFont(fieldFont);
        formPanel.add(nameField, gbc);

        // Guest ID with verification
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField();
        idField.setPreferredSize(fieldSize);
        idField.setFont(fieldFont);
        
        // Add ID verification label
        JLabel idVerificationLabel = new JLabel(" ");
        idVerificationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        idVerificationLabel.setForeground(Color.GRAY);
        
        // Add ID verification on focus lost
        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    if (guestService.isGuestIdRegistered(id)) {
                        idVerificationLabel.setText("This ID is already registered");
                        idVerificationLabel.setForeground(Color.RED);
                    } else {
                        idVerificationLabel.setText("ID is available");
                        idVerificationLabel.setForeground(new Color(46, 204, 113));
                    }
                } catch (NumberFormatException ex) {
                    idVerificationLabel.setText("Please enter a valid number");
                    idVerificationLabel.setForeground(Color.RED);
                }
            }
        });
        
        formPanel.add(idField, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(idVerificationLabel, gbc);

        // Contact
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(contactLabel, gbc);
        gbc.gridx = 1;
        JTextField contactField = new JTextField();
        contactField.setPreferredSize(fieldSize);
        contactField.setFont(fieldFont);
        formPanel.add(contactField, gbc);

        // Register button with modern design
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        JButton registerButton = new JButton("Register Guest");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setPreferredSize(new Dimension(200, 40));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(52, 152, 219));
            }
        });

        registerButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String idText = idField.getText().trim();
                String contact = contactField.getText().trim();
                
                // Validate inputs
                if (name.isEmpty() || idText.isEmpty() || contact.isEmpty()) {
                    showError("Please fill all fields");
                    return;
                }

                if (!name.matches("^[a-zA-Z ]+$")) {
                    showError("Name should only contain letters and spaces");
                    return;
                }

                if (!contact.matches("^[0-9]{10}$")) {
                    showError("Contact number should be 10 digits");
                    return;
                }

                int id = Integer.parseInt(idText);
                if (guestService.isGuestIdRegistered(id)) {
                    showError("This ID is already registered");
                    return;
                }

                guestService.registerGuest(name, id, contact, 0);
                showSuccess("Guest registered successfully!");
                
                // Clear fields
                nameField.setText("");
                idField.setText("");
                contactField.setText("");
                idVerificationLabel.setText(" ");
                
                // Show welcome screen after successful registration
                cardLayout.show(mainPanel, "WELCOME");
            } catch (NumberFormatException ex) {
                showError("Please enter a valid ID number");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });
        formPanel.add(registerButton, gbc);

        // Center the form panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 240));
        centerPanel.add(formPanel);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Title
        JLabel titleLabel = new JLabel("View Guest Bookings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        searchPanel.add(new JLabel("Enter Guest ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(BACKGROUND_COLOR);
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            try {
                int guestId = Integer.parseInt(searchField.getText().trim());
                Guest guest = guestService.searchGuestById(guestId);
                if (guest != null) {
                    List<Booking> bookings = bookingService.getBookingsByGuest(guest);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Bookings for Guest: ").append(guest.getName()).append("\n\n");
                    if (bookings.isEmpty()) {
                        sb.append("No bookings found for this guest.");
                    } else {
                        for (Booking booking : bookings) {
                            sb.append("Room: ").append(booking.getRoom().getRoomNumber())
                              .append(" (").append(booking.getRoom().getCategory()).append(")\n");
                        }
                    }
                    resultsArea.setText(sb.toString());
                } else {
                    resultsArea.setText("Guest not found.");
                }
            } catch (NumberFormatException ex) {
                resultsArea.setText("Please enter a valid guest ID.");
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(resultsPanel, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setBackground(PRIMARY_COLOR);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            loggedInGuest = null;
            cardLayout.show(mainPanel, "LOGIN");
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);
        
        JMenu viewMenu = new JMenu("View");
        JMenuItem welcomeItem = new JMenuItem("Welcome");
        JMenuItem roomsItem = new JMenuItem("Rooms");
        JMenuItem bookingItem = new JMenuItem("Booking");
        JMenuItem guestsItem = new JMenuItem("Guests");
        
        welcomeItem.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        roomsItem.addActionListener(e -> cardLayout.show(mainPanel, "ROOMS"));
        bookingItem.addActionListener(e -> cardLayout.show(mainPanel, "BOOKING"));
        guestsItem.addActionListener(e -> cardLayout.show(mainPanel, "GUESTS"));
        
        viewMenu.add(welcomeItem);
        viewMenu.add(roomsItem);
        viewMenu.add(bookingItem);
        viewMenu.add(guestsItem);
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        
        setJMenuBar(menuBar);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
} 