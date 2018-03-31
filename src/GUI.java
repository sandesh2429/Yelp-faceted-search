
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class GUI {
	public static void main(String arg[]) {
		GUIManager.createGUI();
	}

	public static class GUIManager {
		static JFrame mainFrame;
		static JPanel mainCategoryListPanel = new JPanel();
		static JPanel subCategoryListPanel = new JPanel();
		static JPanel attributeListPanel = new JPanel();
		
		
		static JTable resultTable;
		static DefaultTableModel resultDatatable;
		static DefaultTableModel userDatatable;
		
		static JComboBox<String> locationDropDown;
		static JComboBox<String> dayDropDown;
		static JComboBox<String> fromDropDown;
		static JComboBox<String> toDropDown;
		static JComboBox<String> searchDropDown;

		//sandesh user
		static JComboBox<String> reviewCountDropDown;
		static JComboBox<String> noOfFriendsDropDown;
		static JComboBox<String> avgStarsDropDown;
		static JComboBox<String> AndOrDropDown;
		static JTextField reviewCountValue;
		static JTextField noOfFriendsValue;
		static JTextField avgStarsValue;

		//review
		static JComboBox<String> starCountDropDown;
		static JComboBox<String> noOfVotesDropDown;
		static JTextField starCountValue;
		static JTextField noOfVotesValue;
		static JTextField valueTextField;

		//checkin


	  static JComboBox <String> cheats;
	  static JComboBox <String> toComboBox;
	  static JComboBox <String> totimeComboBox;
	  static JComboBox <String> numCheckinComboBox;
	  static JComboBox <String> startsComboBox;
	  static JComboBox <String> votesComboBox;
	  static JComboBox <String> reviewCountLabelComboBox;
	  static JComboBox <String> friendsCountLabelComboBox;
	  static JComboBox <String> fromdayComboBox;
	  static JComboBox <String> fromtimeComboBox;
	  static JComboBox <String> todayComboBox;

	  static JSpinner yelpingSinceDatespinner;
	  static JSpinner todatespinner;
	  static JSpinner fromdatespinner;

	  

		
		public static final int MAIN_CATEGORY_LIST = 0;
		public static final int SUB_CATEGORY_LIST = 1;
		public static final int ATTRIBUTE_LIST = 2;
		
		public static void createGUI() {
			mainFrame = new JFrame();
			
			//
			addComponentsToPane(mainFrame.getContentPane());
			//Show Window
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setTitle("Database Homework 3 : Sandesh Manjarekar");
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			mainFrame.setVisible(true);
		
			//Reset attribute list
			GUIManager.setMainCategory(DBManager.getAllMainCategories());
			GUIManager.setSubCategory(null);
			GUIManager.setAttributes(null);
			GUIManager.setLocation(null);
			GUIManager.setDayOfWeek(null);
			GUIManager.setFromHours(null, null);
			GUIManager.setToHours(null, null);
		}
		
		public static void showReviewFrame(String bid) {
			JFrame reviewFrame = new JFrame();
			
			JPanel reviewResultPanel = new JPanel();
			
			String columns[] = {"Review Date","Stars","Review Text","UserID","Useful Votes","Funny Votes","Cool Votes"};
			
			JTable reviewTable = new JTable();
			DefaultTableModel reviewDatatable = new DefaultTableModel();
			
			for(String columnName : columns)
				reviewDatatable.addColumn(columnName);
			reviewTable.setModel(reviewDatatable);
			
			List<List<String>> reviewList = DBManager.getReviews(bid); 
			for(List<String> row : reviewList) {
				reviewDatatable.addRow(row.toArray());
			}
			
			JScrollPane sp = new JScrollPane(reviewTable);
			
			reviewResultPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			reviewResultPanel.add(sp,c);
			reviewFrame.add(reviewResultPanel,BorderLayout.CENTER);
			reviewFrame.setTitle("Reviews : " + bid);
			reviewFrame.setSize(800,600);
			reviewFrame.setVisible(true);
		}

		public static void showReviewFrameForUser(String userid) {
			JFrame reviewFrame = new JFrame();
			
			JPanel reviewResultPanel = new JPanel();
			
			String columns[] = {"Review Date","Stars","Review Text","bid","Useful Votes","Funny Votes","Cool Votes"};
			
			JTable reviewTable = new JTable();
			DefaultTableModel reviewDatatable = new DefaultTableModel();
			
			for(String columnName : columns)
				reviewDatatable.addColumn(columnName);
			reviewTable.setModel(reviewDatatable);
			
			//List<List<String>> reviewList = DBManager.getReviews(bid); 

			List<List<String>> userReviewList = new ArrayList<List<String>>();
			try {
				Statement stmt = DBManager.conn.createStatement();
				int noOfFields = 7;
				ResultSet rs = stmt.executeQuery("SELECT ReviewDate, Stars, ReviewText, bid, USEFUL_COUNT, FUNNY_COUNT, COOL_COUNT from yelp_review WHERE userid = '" + userid + "' ORDER BY REVIEWDATE");
				
				List<String> row;
				while(rs.next()) {
					row = new ArrayList<String>();
					for(int j = 1; j <= noOfFields; j++) {
						row.add(rs.getString(j));
					}
					userReviewList.add(row);
				}
			} catch(Exception e) {
				System.out.println("get Review error " + e);
			}

			//displaying reviews
			for(List<String> row : userReviewList) {
				reviewDatatable.addRow(row.toArray());
			}
			
			JScrollPane sp = new JScrollPane(reviewTable);
			
			reviewResultPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			reviewResultPanel.add(sp,c);
			reviewFrame.add(reviewResultPanel,BorderLayout.CENTER);
			reviewFrame.setTitle("Reviews : " + userid);
			reviewFrame.setSize(800,600);
			reviewFrame.setVisible(true);
		}
		
		public static void addComponentsToPane(Container pane) {
			//Default Layout is Border Layout
			
			//Grid Bag Layout Constraints
			GridBagConstraints c = new GridBagConstraints();
			
			//Create Main JPanel
			JPanel mainPanel = new JPanel();
			
			JPanel topPanel = createTopPanel();
			JPanel bottomPanel = createBottomPanel();
			JPanel FooterPanel = createFooterPanel();
			
			//Panel Layout to GridLayout
			mainPanel.setLayout(new GridBagLayout());
			
			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy = 0;
			c.weighty = 0.5;	//80% of Height
			c.weightx = 1;
			mainPanel.add(topPanel,c);
			
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 0.1;	//20% of Height
			c.gridx = 0;
			c.gridy = 1;
			mainPanel.add(bottomPanel,c);

			c.fill = GridBagConstraints.BOTH;
			c.weighty = 0.5;	
			c.gridx = 0;
			c.gridy = 2;
			mainPanel.add(FooterPanel,c);
			
			pane.add(mainPanel,BorderLayout.CENTER);
		}
		
		public static JPanel createTopPanel() {
				//Create Top Panel
				JPanel topPanel = new JPanel();
				//topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				topPanel.setLayout(new GridBagLayout());
				
				//Main Category List
				JPanel mainCategoryPanel = GUIManager.createListPanel(GUIManager.MAIN_CATEGORY_LIST);
				
				//Sub Category List
				JPanel subCategoryPanel = GUIManager.createListPanel(GUIManager.SUB_CATEGORY_LIST);
				
				//Attribute List
				JPanel attributePanel = GUIManager.createListPanel(GUIManager.ATTRIBUTE_LIST);
				
				//Business Result List
				JPanel businessResultPanel = new JPanel();
				JLabel businessResultLabel = new JLabel("Business Result");
				businessResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
				
				String columns[] = {"","Business Id","Name","Address","City","State","# of Reviews","Stars","# of Checkins"};
				
				resultTable = new JTable();
				resultTable.addMouseListener(GUIController.tableMouseListener);
				resultDatatable = new DefaultTableModel() {
					private static final long serialVersionUID = 1L;

					@Override
				    public boolean isCellEditable(int row, int column) {
				       //all cells false
				       return false;
				    }
				};
				for(String columnName : columns)
					resultDatatable.addColumn(columnName);
				resultTable.setModel(resultDatatable);
				JScrollPane sp = new JScrollPane(resultTable);
				
				businessResultPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.HORIZONTAL;
				c.weightx = 1;
				businessResultPanel.add(businessResultLabel,c);
				
				c.gridx = 0;
				c.gridy = 1;
				c.fill = GridBagConstraints.BOTH;
				c.weighty = 1;
				businessResultPanel.add(sp,c);
				
				//businessResultPanel.setBorder(BorderFactory.createLineBorder(Color.RED));


			    JPanel businessTitlePanel = new JPanel();
			    JLabel businessPanelHeadingLabel = new JLabel("Business");
				businessTitlePanel.setBackground(Color.lightGray);
				//userPanelHeading.setHorizontalAlignment(SwingConstants.CENTER);
				businessTitlePanel.add(businessPanelHeadingLabel);
				JPanel businessContent = new JPanel();
		    	JSplitPane businessPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, businessTitlePanel, businessContent);

		    	businessContent.setLayout(new GridBagLayout());

		    	c.gridx = 0;
			    c.gridy = 0;
			    c.weightx = 0.50;
			    c.fill = GridBagConstraints.BOTH;
			    topPanel.add(businessPanel,c);
			    c.gridx = 1;
				c.weightx = 0.50;
				topPanel.add(businessResultPanel,c);
					
				
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 0.50;	//33% of 50% Screen Width
				businessContent.add(mainCategoryPanel,c);
				c.gridx = 1;
				c.gridy = 0;
				c.weightx = 0.50;	//33% of 50% Screen Width
				businessContent.add(subCategoryPanel,c);
				c.gridx = 2;
				c.weightx = 0.15;	//33% of 50% Screen Width
				businessContent.add(attributePanel,c);
				
				
				return topPanel;
		}
		
		public static JPanel createBottomPanel() {
			JPanel bottomPanel = new JPanel();
			GridBagConstraints c = new GridBagConstraints();
			
			//bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			bottomPanel.setLayout(new GridBagLayout());
			
			//Add Location Drop Down
			JPanel locationPanel = new JPanel();
			
			String[] p = {"Any"};
			locationDropDown = new JComboBox<String>(p);
			locationDropDown.addActionListener(GUIController.locationActionListener);
			JLabel locationLabel = new JLabel("Location");
			
			locationPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			locationPanel.add(locationLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			locationPanel.add(locationDropDown,c);
			
			
			//Add Day Drop Down
			JPanel dayPanel = new JPanel();
			
			dayDropDown = new JComboBox<String>(p);
			dayDropDown.addActionListener(GUIController.dayActionListener);
			JLabel dayLabel = new JLabel("Day of the Week");
			
			dayPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			dayPanel.add(dayLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			dayPanel.add(dayDropDown,c);
			
			//Add From Drop Down
			JPanel fromPanel = new JPanel();
			
			fromDropDown = new JComboBox<String>(p);
			fromDropDown.addActionListener(GUIController.fromActionListener);
			JLabel fromLabel = new JLabel("From");
			
			fromPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			fromPanel.add(fromLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			fromPanel.add(fromDropDown,c);
			
			//Add To Drop Down
			JPanel toPanel = new JPanel();
			
			toDropDown = new JComboBox<String>(p);
			toDropDown.addActionListener(GUIController.toActionListener);
			JLabel toLabel = new JLabel("To");
			
			toPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			toPanel.add(toLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			toPanel.add(toDropDown,c);
			
			//Add Search For Drop Down
			JPanel searchPanel = new JPanel();
			
			String[] searchOptions = {"Any Attribute","All Attributes"};
			searchDropDown = new JComboBox<String>(searchOptions);
			searchDropDown.addActionListener(GUIController.searchForActionListener);
			JLabel searchLabel = new JLabel("Search For");
			
			searchPanel.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.4;
			searchPanel.add(searchLabel,c);
			c.weightx = 0.6;
			c.gridy = 1;
			searchPanel.add(searchDropDown,c);
			
			//Add Search Button
			JButton search = new JButton("Search for Business");
			search.addActionListener(GUIController.searchButtonActionListener);
			bottomPanel.setLayout(new GridBagLayout());
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.fill = GridBagConstraints.NONE;
			c.ipadx = 150;
			bottomPanel.add(locationPanel,c);
			//Add Day Drop Down
			c.gridx = 1;
			bottomPanel.add(dayPanel,c);
			c.gridx = 2;
			bottomPanel.add(fromPanel,c);
			c.gridx = 3;
			bottomPanel.add(toPanel,c);
			c.gridx = 4;
			bottomPanel.add(searchPanel,c);
			c.gridx = 5;
			c.fill = GridBagConstraints.VERTICAL;
			bottomPanel.add(search,c);
			
			return bottomPanel;
		}

		public static JPanel createFooterPanel(){
			JPanel footerPanel = new JPanel();
			footerPanel.setLayout(new GridBagLayout());

			//user area

			//heading
			//JList<String> mainCategoryList = new JList<String>();

			JPanel UsersTitlePanel = new JPanel();
			JLabel userPanelHeadingLabel = new JLabel("Users");
			UsersTitlePanel.setBackground(Color.lightGray);
			//userPanelHeading.setHorizontalAlignment(SwingConstants.CENTER);
			UsersTitlePanel.add(userPanelHeadingLabel);
			JPanel UserContent = new JPanel();
	    	JSplitPane UserPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, UsersTitlePanel, UserContent);



	    	JPanel reviewTitlePanel = new JPanel();
			JLabel reviewPanelHeadingLabel = new JLabel("Review");
			reviewTitlePanel.setBackground(Color.lightGray);
			//userPanelHeading.setHorizontalAlignment(SwingConstants.CENTER);
			reviewTitlePanel.add(reviewPanelHeadingLabel);
			JPanel reviewContent = new JPanel();
	    	JSplitPane reviewPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, reviewTitlePanel, reviewContent);


	    	JPanel checkinTitlePanel = new JPanel();
			JLabel checkinPanelHeadingLabel = new JLabel("checkin");
			checkinTitlePanel.setBackground(Color.lightGray);
			//userPanelHeading.setHorizontalAlignment(SwingConstants.CENTER);
			checkinTitlePanel.add(checkinPanelHeadingLabel);
			JPanel checkinContent = new JPanel();
	    	JSplitPane checkinPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, checkinTitlePanel, checkinContent);

	    	GridBagConstraints c = new GridBagConstraints();

			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0.20;
			c.fill = GridBagConstraints.BOTH;
			footerPanel.add(UserPanel,c);
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 0.20;
			c.fill = GridBagConstraints.BOTH;
			footerPanel.add(reviewPanel,c);
			c.gridx = 2;
			c.gridy = 0;
			c.weightx = 0.20;
			c.fill = GridBagConstraints.BOTH;
			footerPanel.add(checkinPanel,c);
			c.gridx = 3;
			c.gridy = 0;
			c.weightx = 0.40;
			c.fill = GridBagConstraints.BOTH;
			footerPanel.add(new JPanel(),c);

			//Value label for all
			 c = new GridBagConstraints();
			
			//User Contents

			JLabel reviewCount = new JLabel("Review Count");
			String[] reviewCountCondtn = {"Any","<","<=","=",">=",">"};
			reviewCountDropDown = new JComboBox<String>(reviewCountCondtn);
			reviewCountValue =  new JTextField(2);

			JLabel noOfFriends = new JLabel("No Of Friends");
			String[] noOfFriendsCondtn = {"Any","<","<=","=",">=",">"};
			noOfFriendsDropDown = new JComboBox<String>(noOfFriendsCondtn);
			noOfFriendsValue =  new JTextField(2);


			JLabel avgStars = new JLabel("Avg Stars");
			String[] avgStarsCondtn = {"Any","<","<=","=",">=",">"};
			avgStarsDropDown = new JComboBox<String>(avgStarsCondtn);
			avgStarsValue =  new JTextField(2);


			JLabel yelpingsinceLabel = new JLabel("Yelping Since");

		     Date yelpingSinceDate = new Date();
		     SpinnerDateModel yelpingSinceDatesm = new SpinnerDateModel(yelpingSinceDate, null, null, Calendar.HOUR_OF_DAY);
		     yelpingSinceDatespinner = new JSpinner(yelpingSinceDatesm);
		     JSpinner.DateEditor yelpingSinceDatede = new JSpinner.DateEditor(yelpingSinceDatespinner, "yyyy-MM");
		     yelpingSinceDatespinner.setEditor(yelpingSinceDatede);

			JLabel AndOrLabel = new JLabel("    Select : ");
			String[] AndOrCondtn = {"AND","OR"};
			AndOrDropDown = new JComboBox<String>(AndOrCondtn);


			// Search Button For User
			JButton userSearchBtn = new JButton("Search");
			userSearchBtn.addActionListener(GUIController.userSearchBtnActionListener);

			UserContent.setLayout(new GridBagLayout()); 
			UserContent.setBorder(new EmptyBorder(10, 10, 10, 10));

			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.15;
			UserContent.add(reviewCount,c);
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(0, 0, 3, 0);
			UserContent.add(reviewCountDropDown,c);
			c.gridx = 2;
			c.gridy = 0;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			UserContent.add(new JLabel("     Value : "),c);
			c.gridx = 3;
			c.gridy = 0;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			UserContent.add(reviewCountValue,c);


			c.gridx = 0;
			c.gridy = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.15;
			UserContent.add(noOfFriends,c);
			c.gridx = 1;
			c.gridy = 1;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(0, 0, 3, 0);
			UserContent.add(noOfFriendsDropDown,c);
			c.gridx = 2;
			c.gridy = 1;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			UserContent.add(new JLabel("     Value : "),c);
			c.gridx = 3;
			c.gridy = 1;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			UserContent.add(noOfFriendsValue,c);


			c.gridx = 0;
			c.gridy = 2;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.15;
			UserContent.add(avgStars,c);
			c.gridx = 1;
			c.gridy = 2;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			UserContent.add(avgStarsDropDown,c);
			c.gridx = 2;
			c.gridy = 2;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			UserContent.add(new JLabel("     Value : "),c);
			c.gridx = 3;
			c.gridy = 2;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			UserContent.add(avgStarsValue,c);

			c.gridx = 0;
			c.gridy = 3;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.45;
			UserContent.add(yelpingsinceLabel,c);
			c.gridx = 1;
			c.gridy = 3;
			c.weightx = 0.55;
			c.fill = GridBagConstraints.HORIZONTAL;
			UserContent.add(yelpingSinceDatespinner,c);

			c.gridx = 0;
			c.gridy = 4;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.50;
			UserContent.add(AndOrLabel,c);
			c.gridx = 1;
			c.gridy = 4;
			c.weightx = 0.35;
			c.fill = GridBagConstraints.HORIZONTAL;
			UserContent.add(AndOrDropDown,c);
			c.gridx = 2;
			c.weightx = 0.15;
			UserContent.add(new JPanel(),c);

			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 0.25;
			UserContent.add(new JPanel(),c);

			c.gridx = 1;
			c.gridy = 5;
			c.weightx = 0.50;
			c.insets = new Insets(5, 2, 2, 2);
			UserContent.add(userSearchBtn,c);

			c.gridx = 2;
			c.gridy = 5;
			c.weightx = 0.25;
			UserContent.add(new JPanel(),c);
			


		//Review content


			JLabel starCount = new JLabel("Stars : ");
			String[] starCountCondtn = {"Any","<","<=","=",">=",">"};
			starCountDropDown = new JComboBox<String>(starCountCondtn);
			starCountValue =  new JTextField(2);

			JLabel noOfVotes = new JLabel("No Of Votes");
			String[] noOfVotesCondtn = {"Any","<","<=","=",">=",">"};
			noOfVotesDropDown = new JComboBox<String>(noOfVotesCondtn);
			noOfVotesValue =  new JTextField(2);

			 JLabel fromdateLabel = new JLabel("From Date :  ");

			 Date fromdatedate = new Date();
		     SpinnerDateModel fromdatesm = new SpinnerDateModel(fromdatedate, null, null, Calendar.HOUR_OF_DAY);
		     fromdatespinner = new JSpinner(fromdatesm);
		     JSpinner.DateEditor fromdatede = new JSpinner.DateEditor(fromdatespinner, "yyyy-MM-dd");
		     fromdatespinner.setEditor(fromdatede);
		     
		     //reviewPanel.add(fromdatespinner);

		     JLabel todateLabel = new JLabel("To Date");

		     Date todatedate = new Date();
		     SpinnerDateModel todatesm = new SpinnerDateModel(todatedate, null, null, Calendar.HOUR_OF_DAY);
		     //JSpinner todatespin = new JSpinner(todatesm);
		     todatespinner = new JSpinner(todatesm);
		     JSpinner.DateEditor todatede = new JSpinner.DateEditor(todatespinner, "yyyy-MM-dd");
		     todatespinner.setEditor(todatede);
		     //reviewPanel.add(todatespinner);



			// Search Button For Review
			JButton reviewSearchBtn = new JButton("Search");
			reviewSearchBtn.addActionListener(GUIController.reviewSearchBtnActionListener);



			reviewContent.setLayout(new GridBagLayout()); 
			reviewContent.setBorder(new EmptyBorder(10, 10, 10, 10));

			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.25;
			reviewContent.add(fromdateLabel,c);

			c.gridx = 1;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.75;
			reviewContent.add(fromdatespinner,c);

			c.gridx = 0;
			c.gridy = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.25;
			reviewContent.add(todateLabel,c);

			c.gridx = 1;
			c.gridy = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.75;
			reviewContent.add(todatespinner,c);



			c.gridx = 0;
			c.gridy = 2;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.15;
			reviewContent.add(starCount,c); 
			c.gridx = 1;
			c.gridy = 2;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(0, 0, 8, 0);
			reviewContent.add(starCountDropDown,c);
			c.gridx = 2;
			c.gridy = 2;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			reviewContent.add(new JLabel("     Value : "),c);
			c.gridx = 3;
			c.gridy = 2;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			reviewContent.add(starCountValue,c);

			c.gridx = 0;
			c.gridy = 3;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.15;
			reviewContent.add(noOfVotes,c);
			c.gridx = 1;
			c.gridy = 3;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(0, 0, 8, 0);
			reviewContent.add(noOfVotesDropDown,c);
			c.gridx = 2;
			c.gridy = 3;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			reviewContent.add(new JLabel("     Value : "),c);
			c.gridx = 3;
			c.gridy = 3;
			c.weightx = 0.15;
			c.fill = GridBagConstraints.HORIZONTAL;
			reviewContent.add(noOfVotesValue,c);

			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 0.25;
			reviewContent.add(new JPanel(),c);

			c.gridx = 1;
			c.gridy = 4;
			c.weightx = 0.50;
			c.insets = new Insets(15, 2, 2, 2);
			reviewContent.add(reviewSearchBtn,c);

			c.gridx = 2;
			c.gridy = 4;
			c.weightx = 0.25;
			reviewContent.add(new JPanel(),c);

		//CheckIn content
			
			JLabel anylabel = new JLabel("Checkin");
			Font f = anylabel.getFont();

			 JPanel CheckinPanel = new JPanel();
			 checkinContent.setLayout(new GridBagLayout()); 


		     CheckinPanel.setLayout(new BoxLayout(CheckinPanel, BoxLayout.Y_AXIS));
		     JLabel CheckinLabel = new JLabel("Checkin");
		     CheckinLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		     JPanel CheckinTitle = new JPanel();
		     CheckinTitle.setBackground(Color.lightGray);
		     CheckinTitle.add(CheckinLabel);
		     JScrollPane CheckinContents = new JScrollPane();
		     JSplitPane CheckinPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, CheckinTitle, CheckinContents);
		     CheckinPane.setEnabled(false);
		     CheckinContents.setViewportView(CheckinPanel);
		     
		     JLabel fromLabel = new JLabel("From Day");
		    // CheckinPanel.add(fromLabel);
		     
		     fromdayComboBox = new JComboBox<>();
		     fromdayComboBox.addItem("Select");
		     fromdayComboBox.addItem("Monday");
		     fromdayComboBox.addItem("Tuesday");
		     fromdayComboBox.addItem("Wednesday");
		     fromdayComboBox.addItem("Thursday");
		     fromdayComboBox.addItem("Friday");
		     fromdayComboBox.addItem("Saturday");
		     fromdayComboBox.addItem("Sunday");
		     
		     //CheckinPanel.add(fromdayComboBox);
		     fromdayComboBox.setSelectedIndex(0);
		     
		     JLabel fromTimeLabel = new JLabel("From Time");
		     CheckinPanel.add(fromTimeLabel);
		    
		     fromtimeComboBox = new JComboBox<>();
		     fromtimeComboBox.addItem("Select");
		     for(int i = 0;i<24;i++) {
		    	 fromtimeComboBox.addItem((Integer.toString(i)));
		     }
		     //CheckinPanel.add(fromtimeComboBox);

		    // checkinContent.add(CheckinPanel);
		     //- - - - - - - -  - - -


		     JLabel toLabel = new JLabel("To Day");
		     //CheckinPanel.add(toLabel);
		     
		     todayComboBox = new JComboBox<>();
		     todayComboBox.addItem("Select");
		     todayComboBox.addItem("Monday");
		     todayComboBox.addItem("Tuesday");
		     todayComboBox.addItem("Wednesday");
		     todayComboBox.addItem("Thursday");
		     todayComboBox.addItem("Friday");
		     todayComboBox.addItem("Saturday");
		     todayComboBox.addItem("Sunday");
		     //CheckinPanel.add(todayComboBox);
		     todayComboBox.setSelectedItem(0);

		     JLabel toTimeLabel = new JLabel("To Time");
		     //CheckinPanel.add(toTimeLabel);
		     
		     totimeComboBox = new JComboBox<>();
		     totimeComboBox.addItem("Select");
		     for(int i =0;i<24;i++) {
		    	 totimeComboBox.addItem((Integer.toString(i)));
		     }
		   //  CheckinPanel.add(totimeComboBox);
		    // totimeComboBox.setSelectedItem(0);

		     JLabel numCheckinLabel = new JLabel("Num. of Checkins:");
		     //CheckinPanel.add(numCheckinLabel);
		     
		     numCheckinComboBox = new JComboBox<>();
		     numCheckinComboBox.addItem("Any");
		     numCheckinComboBox.addItem("=");
		     numCheckinComboBox.addItem(">");
		     numCheckinComboBox.addItem("<");

		     //CheckinPanel.add(numCheckinComboBox);
		     numCheckinComboBox.setSelectedItem(0);

		     JLabel valueLabel = new JLabel("Value:");
		     //CheckinPanel.add(valueLabel);
		     
		     valueTextField = new JTextField();
		     //CheckinPanel.add(valueTextField);

		     JButton checkInSearchBtn = new JButton("Search");
			checkInSearchBtn.addActionListener(GUIController.checkInSearchBtnActionListener);




		     c.gridx = 0;
			 c.gridy = 0;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.45;
			 checkinContent.add(fromLabel,c);

			 c.gridx = 1;
			 c.gridy = 0;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.55;
			 checkinContent.add(fromdayComboBox,c);

			 c.gridx = 0;
			 c.gridy = 1;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.45;
			 checkinContent.add(fromTimeLabel,c);

			 c.gridx = 1;
			 c.gridy = 1;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.55;
			 checkinContent.add(fromtimeComboBox,c);

			 c.gridx = 0;
			 c.gridy = 2;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.45;
			 checkinContent.add(toLabel,c);

			 c.gridx = 1;
			 c.gridy = 2;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.55;
			 checkinContent.add(todayComboBox,c);

			 c.gridx = 0;
			 c.gridy = 3;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.45;
			 checkinContent.add(toTimeLabel,c);

			 c.gridx = 1;
			 c.gridy = 3;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.55;
			 checkinContent.add(totimeComboBox,c);

			 //
			 c.gridx = 0;
			 c.gridy = 4;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.25;
			 checkinContent.add(numCheckinLabel,c);

			 c.gridx = 1;
			 c.gridy = 4;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.25;
			 checkinContent.add(numCheckinComboBox,c);

			 c.gridx = 2;
			 c.gridy = 4;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.25;
			 checkinContent.add(valueLabel,c);

			 c.gridx = 3;
			 c.gridy = 4;
			 c.fill = GridBagConstraints.HORIZONTAL;
			 c.weightx = 0.25;
			 checkinContent.add(valueTextField,c);

			 c.gridx = 0;
			c.gridy = 5;
			c.weightx = 0.25;
			checkinContent.add(new JPanel(),c);

			c.gridx = 1;
			c.gridy = 5;
			c.weightx = 0.50;
			c.insets = new Insets(15, 2, 2, 2);
			checkinContent.add(checkInSearchBtn,c);

			c.gridx = 2;
			c.gridy = 5;
			c.weightx = 0.25;
			checkinContent.add(new JPanel(),c);





			return footerPanel;
		}
		
		public static JPanel createListPanel(int type) {
			//Create Components
			
			//1. Heading
			JLabel panelHeading = new JLabel();
			if(type == MAIN_CATEGORY_LIST) panelHeading.setText("Main Categories");
			else if(type == SUB_CATEGORY_LIST) panelHeading.setText("Sub Categories");
			else if(type == ATTRIBUTE_LIST) panelHeading.setText("Attributes");
			panelHeading.setHorizontalAlignment(SwingConstants.CENTER);
			
			//2. List of Categories
			JPanel panelList = null;
			
			if(type == MAIN_CATEGORY_LIST)
				panelList = mainCategoryListPanel;
			else if(type == SUB_CATEGORY_LIST)
				panelList = subCategoryListPanel;
			else if(type == ATTRIBUTE_LIST)
				panelList = attributeListPanel;
			JScrollPane listScrollPanel = new JScrollPane(panelList);
			panelList.setBackground(Color.WHITE);
			panelList.setLayout(new BoxLayout(panelList,  BoxLayout.PAGE_AXIS));
			
			//Main Category Panel
			JPanel panel = new JPanel();
			//panel.setBorder(BorderFactory.createLineBorder(Color.RED));
			panel.setLayout(new GridBagLayout());
			
			//Put Components together
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1;
			
			panel.add(panelHeading,c); //Heading
			c.gridx = 0;
			c.gridy = 1;
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 1;
			panel.add(listScrollPanel,c); //List
			
			return panel;
		}
		public static JPanel createMainCategoryPanel() {
			return null;
		}
		
		public static void setMainCategory(List<String> mainCategories) {
			mainCategoryListPanel.removeAll();
			
			for(String option : mainCategories) {
				JCheckBox temp = new JCheckBox(option);
				temp.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
				temp.setBackground(Color.WHITE);
				temp.addItemListener(GUIController.mainCategoryCBListener);
				mainCategoryListPanel.add(temp);
			}
			
			mainCategoryListPanel.revalidate();
			mainCategoryListPanel.repaint();
		}
		
		public static void setSubCategory(List<String> subCategories) {
			subCategoryListPanel.removeAll();
			
			if(subCategories != null) {
				for(String option : subCategories) {
					JCheckBox temp = new JCheckBox(option);
					temp.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
					temp.setBackground(Color.WHITE);
					temp.addItemListener(GUIController.subCategoryCBListener);
					subCategoryListPanel.add(temp);
				}
			}
			
			subCategoryListPanel.revalidate();
			subCategoryListPanel.repaint();
		}
		
		public static void setAttributes(List<String> attributeList) {
			attributeListPanel.removeAll();
			
			if(attributeList != null) {
				for(String option : attributeList) {
					JCheckBox temp = new JCheckBox(option);
					temp.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
					temp.setBackground(Color.WHITE);
					temp.addItemListener(GUIController.attributeCBListener);
					attributeListPanel.add(temp);
				}
			}
			
			attributeListPanel.revalidate();
			attributeListPanel.repaint();
		}
		public static void setResultTable(List<List<String>> businessList) {
			
			//Remove All Rows
			while(resultDatatable.getRowCount() > 0)
				resultDatatable.removeRow(0);
			
			for(List<String> row : businessList) {
				resultDatatable.addRow(row.toArray());
			}
			
			resultTable.revalidate();
			resultTable.repaint();
		}
		
		public static void setLocation(List<String> locations) {
			locationDropDown.removeAllItems();
			locationDropDown.addItem("Any");
			locationDropDown.setSelectedItem("Any");
			if(locations != null) {
				for(String location : locations) {
					locationDropDown.addItem(location);
				}
			}
			locationDropDown.revalidate();
			locationDropDown.repaint();
		}
		
		public static void setDayOfWeek(List<String> days) {
			dayDropDown.removeAllItems();
			dayDropDown.addItem("Any");
			dayDropDown.setSelectedItem("Any");
			System.out.println("Setting days");
			if(days!=null) {
				for(String day : days) {
					dayDropDown.addItem(day);
				}
			}
			dayDropDown.revalidate();
			dayDropDown.repaint();
		}
		
		public static void setFromHours(Integer minRange, Integer maxRange) {
			fromDropDown.removeAllItems();
			fromDropDown.addItem("Any");
			fromDropDown.setSelectedItem("Any");
			if(minRange != null && maxRange != null) {
				int minHour = Math.floorDiv(minRange, 60);
				int maxHour = Math.floorDiv(maxRange, 60) + 1;
				for(int i = minHour; i <= maxHour; i++) {
					if(i<24) {
						fromDropDown.addItem(i + ":00");
					}
				}
			}
			fromDropDown.revalidate();
			fromDropDown.repaint();
		}
		
		public static void setToHours(Integer minRange, Integer maxRange) {
			toDropDown.removeAllItems();
			toDropDown.addItem("Any");
			toDropDown.setSelectedItem("Any");
			if(minRange != null && maxRange != null) {
				int minHour = Math.floorDiv(minRange, 60);
				int maxHour = Math.floorDiv(maxRange, 60) + 1;
				for(int i = minHour; i <= maxHour; i++) {
					if(i<24) {
						toDropDown.addItem(i + ":00");
					}
				}
			}
			toDropDown.revalidate();
			toDropDown.repaint();
		}
	}
	
	public static class GUIController {
		public static List<String> selectedMainCategories = new ArrayList<String>();
		public static List<String> selectedSubCategories = new ArrayList<String>();
		public static List<String> selectedAttributes = new ArrayList<String>();
		public static Map<Integer, Map<String, Integer>> dayMap = new HashMap<Integer, Map<String, Integer>>();

		public static String construct = "or";
		public static String location = null;
		public static Integer dayOfWeek = null;
		public static Integer fromHour = null;
		public static Integer toHour = null;
		
		//Main Category Item Listener
		public static ItemListener mainCategoryCBListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox jCB = (JCheckBox)e.getSource();
				//Get the checked/unchecked state of check box
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					selectedMainCategories.remove(jCB.getText());
				} else if(e.getStateChange() == ItemEvent.SELECTED) {
					selectedMainCategories.add(jCB.getText());
				}
				
				//Update subQueryList
				List<String> subCategoriesList = DBManager.getAllSubCategories(selectedMainCategories, construct);
				selectedSubCategories.clear();
				selectedAttributes.clear();
				dayMap.clear();
				location = null;
				dayOfWeek = null;
				fromHour = null;
				toHour = null;
				
				GUIManager.setSubCategory(subCategoriesList);
				//Reset attribute list
				GUIManager.setAttributes(null);
				GUIManager.setLocation(null);
				GUIManager.setDayOfWeek(null);
				GUIManager.setFromHours(null, null);
				GUIManager.setToHours(null, null);
				
			}
		};
		
		//Sub Category Item Listener
		public static ItemListener subCategoryCBListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox jCB = (JCheckBox)e.getSource();
				//Get the checked/unchecked state of check box
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					selectedSubCategories.remove(jCB.getText());
				} else if(e.getStateChange() == ItemEvent.SELECTED) {
					selectedSubCategories.add(jCB.getText());
				}
				
				//Update subQueryList
				List<String> attributeList = DBManager.getAllAttributes(selectedMainCategories,selectedSubCategories, construct);
				selectedAttributes.clear();
				dayMap.clear();
				location = null;
				dayOfWeek = null;
				fromHour = null;
				toHour = null;
				
				GUIManager.setAttributes(attributeList);
				GUIManager.setLocation(null);
				GUIManager.setDayOfWeek(null);
				GUIManager.setFromHours(null, null);
				GUIManager.setToHours(null, null);
			}
		};
		
		//Attribute Item Listener
		public static ItemListener attributeCBListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox jCB = (JCheckBox)e.getSource();
				//Get the checked/unchecked state of check box
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					selectedAttributes.remove(jCB.getText());
				} else if(e.getStateChange() == ItemEvent.SELECTED) {
					selectedAttributes.add(jCB.getText());
				}
				
				//Update location
				List<String> locationList = DBManager.getAllLocations(selectedMainCategories,selectedSubCategories,selectedAttributes,construct);
				location = null;
				GUIManager.setLocation(locationList);
				
				dayMap.clear();
				dayMap = DBManager.getAllDays(selectedMainCategories,selectedSubCategories,selectedAttributes,location,construct);
				dayOfWeek = null;
				List<String> dayList = new ArrayList<String>();
				List<Integer> days = new ArrayList<Integer>();
				days.addAll(dayMap.keySet());
				System.out.println("Attribute: Setting Days" + days);
				days.sort(null);
				for(Integer d : days) {
					String day = "";
					if(d==0) day = "Sunday";
					else if(d==1) day = "Monday";
					else if(d==2) day = "Tuesday";
					else if(d==3) day = "Wednesday";
					else if(d==4) day = "Thursday";
					else if(d==5) day = "Friday";
					else if(d==6) day = "Saturday";
					dayList.add(day);
				}
				System.out.println("Attribute: Setting Days" + dayList);
				GUIManager.setDayOfWeek(null);
				GUIManager.setDayOfWeek(dayList);
				
				dayOfWeek = null;
				fromHour = null;
				toHour = null;
				
				GUIManager.setFromHours(null, null);
				GUIManager.setToHours(null, null);
			}
		};
		
		//Location Action Listener
		public static ActionListener locationActionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>) e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						location = null;
					} else {
						location = (String)jCB.getItemAt(selectedIndex);
					}
					System.out.println("Location" + location);
					//Update location
					
					dayMap.clear();
					dayMap = DBManager.getAllDays(selectedMainCategories,selectedSubCategories,selectedAttributes,location,construct);
					dayOfWeek = null;
					List<String> dayList = new ArrayList<String>();
					List<Integer> days = new ArrayList<Integer>();
					days.addAll(dayMap.keySet());
					System.out.println("Location: Setting Days" + days);
					days.sort(null);
					for(Integer d : days) {
						String day = "";
						if(d==0) day = "Sunday";
						else if(d==1) day = "Monday";
						else if(d==2) day = "Tuesday";
						else if(d==3) day = "Wednesday";
						else if(d==4) day = "Thursday";
						else if(d==5) day = "Friday";
						else if(d==6) day = "Saturday";
						dayList.add(day);
					}
					System.out.println("Location: Setting Days" + dayList);
					GUIManager.setDayOfWeek(dayList);

					fromHour = null;
					toHour = null;
					
					GUIManager.setFromHours(null, null);
					GUIManager.setToHours(null, null);
				}
		};
		
		//Day Action Listener
		public static ActionListener dayActionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>)e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						dayOfWeek = null;
					} else {
						String day = (String)jCB.getItemAt(selectedIndex);
						if(day != null) {
							if(day.equalsIgnoreCase("Sunday")) dayOfWeek = 0;
							else if(day.equalsIgnoreCase("Monday")) dayOfWeek = 1;
							else if(day.equalsIgnoreCase("Tuesday")) dayOfWeek = 2;
							else if(day.equalsIgnoreCase("Wednesday")) dayOfWeek = 3;
							else if(day.equalsIgnoreCase("Thursday")) dayOfWeek = 4;
							else if(day.equalsIgnoreCase("Friday")) dayOfWeek = 5;
							else if(day.equalsIgnoreCase("Saturday")) dayOfWeek = 6;
							else dayOfWeek = null;
						} else {
							dayOfWeek = null;
						}
						
					}
					
					//Update location
					fromHour = null;
					toHour = null;
					if(dayOfWeek == null) {
						fromHour = null;
						toHour = null;
						
						GUIManager.setFromHours(null, null);
						GUIManager.setToHours(null, null);
					} else {
						if(dayMap.containsKey(dayOfWeek)) {
							Integer minHour = dayMap.get(dayOfWeek).get("from");
							Integer maxHour = dayMap.get(dayOfWeek).get("to");
							
							GUIManager.setFromHours(minHour, maxHour);
							GUIManager.setToHours(minHour, maxHour);
						}
					}
				}
			};
			
			//Day Action Listener
			public static ActionListener fromActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>)e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						fromHour = null;
						//System.out.print("From " + fromHour);
					} else {
						String fromH = (String)jCB.getItemAt(selectedIndex);
						if(fromH == null) {
							fromHour = null;
						} else {
							fromHour = Integer.valueOf(fromH.split(":")[0])*60;
							//System.out.print("From " + fromHour);
						}
					}
				}
			};
			
			//To Action Listener
			public static ActionListener toActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>)e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						toHour = null;
						//System.out.print("To " + toHour);
					} else {
						String fromH = (String)jCB.getItemAt(selectedIndex);
						if(fromH == null)
							toHour = null;
						else {
							toHour = Integer.valueOf(fromH.split(":")[0])*60;
							//System.out.print("To " + toHour);
						}
					}
				}
			};
		
		//Search For Item Listener
		public static ActionListener searchForActionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<?> jCB = (JComboBox<?>)e.getSource();
					int selectedIndex = jCB.getSelectedIndex();
					if(selectedIndex == 0) {
						construct = "or";
					} else {
						construct = "and";
					}
					//Update subQueryList
					//List<String> attributeList = DBManager.getAllAttributes(selectedMainCategories,selectedSubCategories);
					//selectedAttributes.clear();
					//GUIManager.setAttributes(attributeList);
					
				}
			};
		
		//Search Button Listener
		public static ActionListener searchButtonActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<List<String>> businessList = DBManager.getAllBusiness(selectedMainCategories,selectedSubCategories, selectedAttributes, location, dayOfWeek, fromHour, toHour, construct);
				GUIManager.setResultTable(businessList);
			}
			
		};


        // Search Button for User Listener
		public static ActionListener userSearchBtnActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame userFrame = new JFrame();			
				JPanel userResultPanel = new JPanel();
				
				String columns[] = {"User_Name","User_Id","Yelping_Since","reviewCount","avg_Stars", "Number_of_Friends"};
				
				JTable userTable = new JTable();
				GUIManager.userDatatable = new DefaultTableModel();
				
				for(String columnName : columns)
					GUIManager.userDatatable.addColumn(columnName);
				userTable.setModel(GUIManager.userDatatable);
				userTable.addMouseListener(GUIController.userTableMouseListener);
				
				List<List<String>> userList = new ArrayList<List<String>>();


				try {
					if(DBManager.conn == null) DBManager.init();
					Statement stmt = DBManager.conn.createStatement();
					int noOfFields = 6;
					String mainQuery = "SELECT u.NAME, u.USERID, u.Yelping_Since, u.Review_count, u.Average_Stars, u.no_of_friends from Yelp_user u ";

					String whereQuery = " where 1=1 ";
					String whereCondition = " AND ";

					//String groupByQuery = " GROUP BY  U.NAME, U.USERID, U.YELPING_SINCE, U.REVIEW_COUNT, U.AVERAGE_STARS ";

					if(GUIManager.AndOrDropDown.getSelectedItem()=="OR")
					{
						whereQuery = " where 1!=1 ";
						 whereCondition = " OR ";
						System.out.println("inside "+whereCondition );
					}


					if(GUIManager.reviewCountDropDown.getSelectedItem()!="Any" 
							&&!GUIManager.reviewCountValue.getText().toString().equals(""))
					{
						whereQuery = whereQuery +whereCondition +" U.REVIEW_COUNT " + GUIManager.reviewCountDropDown.getSelectedItem() +" "+ GUIManager.reviewCountValue.getText();
						//String mainQuery = initialQuery + pseudoWhereQuery + groupBy;
					}
					
					if(GUIManager.avgStarsDropDown.getSelectedItem()!="Any" 
							&&!GUIManager.avgStarsValue.getText().toString().equals(""))
					{
						whereQuery = whereQuery +whereCondition+ " U.AVERAGE_STARS " + GUIManager.avgStarsDropDown.getSelectedItem() +" "+ GUIManager.avgStarsValue.getText();
						//String mainQuery = initialQuery + pseudoWhereQuery + groupBy;
					}

					if(GUIManager.noOfFriendsDropDown.getSelectedItem()!="Any" 
							&& !GUIManager.noOfFriendsValue.getText().toString().equals(""))
					{
						//groupByQuery = groupByQuery + " HAVING COUNT(F.friend_ID) " + GUIManager.noOfFriendsDropDown.getSelectedItem() + " " + GUIManager.noOfFriendsValue.getText();
						//String mainQuery = initialQuery + pseudoWhereQuery + groupBy;
						whereQuery = whereQuery + whereCondition+" U.no_of_friends " + GUIManager.noOfFriendsDropDown.getSelectedItem() +" "+ GUIManager.noOfFriendsValue.getText();
						
					}

					//System.out.println(GUIManager.yelpingSinceDatespinner.getValue().toString());


					    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM");
						String spinnerFromValue = formater.format(GUIManager.yelpingSinceDatespinner.getValue());
						String spinnerToValue = formater.format(GUIManager.yelpingSinceDatespinner.getValue());
						whereQuery = whereQuery +whereCondition+ " TO_DATE(YELPING_SINCE,'YYYY-MM') BETWEEN TO_DATE('" + spinnerFromValue.toString()+ "','YYYY-MM') AND TO_DATE('2018-03','YYYY-MM')";


					 mainQuery = mainQuery +whereQuery;
					
					//____c- - --- - - - - - - - - -  -- - - 
					/*if(GUIManager.reviewCountDropDown.getSelectedItem()!="Any" && GUIManager.reviewCountValue.getText()!="")
					{
						mainQuery = mainQuery + " AND Review_count " + GUIManager.reviewCountDropDown.getSelectedItem() + " " + GUIManager.reviewCountValue.getText();
					}
					
					if(GUIManager.noOfFriendsDropDown.getSelectedItem()!="Any" && GUIManager.noOfFriendsValue.getText()!="")
					{
						mainQuery = mainQuery + " AND Review_count " + GUIManager.noOfFriendsDropDown.getSelectedItem() + " " + GUIManager.noOfFriendsValue.getText();
					}
					if(GUIManager.avgStarsDropDown.getSelectedItem()!="Any" && GUIManager.avgStarsValue.getText()!="")
					{
						mainQuery = mainQuery + " AND average_stars " + GUIManager.avgStarsDropDown.getSelectedItem() + " " + GUIManager.avgStarsValue.getText();
					}*/
					 System.out.println("before "+mainQuery );
					 System.out.println(GUIManager.AndOrDropDown.getSelectedItem() );
					
					

					mainQuery=mainQuery.replaceAll("\\AND\\b","OR");

					
					System.out.println(mainQuery);
					ResultSet rs = stmt.executeQuery(mainQuery);
					
					List<String> row;
					while(rs.next()) {
						row = new ArrayList<String>();
						for(int j = 1; j <= noOfFields; j++) {
							row.add(rs.getString(j));
						}
						userList.add(row);
					}
				} catch(Exception x) {
					System.out.println("get Checkin error " + x.getMessage());x.printStackTrace();
				}




				for(List<String> row : userList) {
					GUIManager.userDatatable.addRow(row.toArray());
				}
				
				JScrollPane sp = new JScrollPane(userTable);
				
				userResultPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1;
				c.weighty = 1;
				userResultPanel.add(sp,c);
				userFrame.add(userResultPanel,BorderLayout.CENTER);
				userFrame.setTitle("YELP USER  : ");
				userFrame.setSize(800,600);
				userFrame.setVisible(true);
				
				
			}
		};


		public static ActionListener reviewSearchBtnActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame reviewFrame = new JFrame();			
				JPanel reviewResultPanel = new JPanel();
				
				String columns[] = {"Review_Id","User_Id","Business_Id","Funny_Count","Useful_count","Cool_count","Stars","Review_date","Review_Text","Total_Votes"};
				
				JTable reviewTable = new JTable();
				DefaultTableModel reviewDatatable = new DefaultTableModel();
				
				for(String columnName : columns)
					reviewDatatable.addColumn(columnName);
				reviewTable.setModel(reviewDatatable);
				
				List<List<String>> reviewList = new ArrayList<List<String>>();;


				try {
					if(DBManager.conn == null) DBManager.init();
					Statement stmt = DBManager.conn.createStatement();
					int noOfFields = 10;
					String mainQuery = "SELECT REVIEWID, USERID, BID ,FUNNY_COUNT, USEFUL_COUNT, COOL_COUNT, STARS, REVIEWDATE, REVIEWTEXT ,FUNNY_COUNT + USEFUL_COUNT + COOL_COUNT SUM1 from Yelp_review where 1=1";
					
					
					if(GUIManager.starCountDropDown.getSelectedItem()!="Any" && !GUIManager.starCountValue.getText().toString().equals(""))
					{
						mainQuery = mainQuery + " AND STARS " + GUIManager.starCountDropDown.getSelectedItem() + " " + GUIManager.starCountValue.getText();
					}
					
					if(GUIManager.noOfVotesDropDown.getSelectedItem()!="Any" && !GUIManager.noOfVotesValue.getText().toString().equals(""))
					{
						mainQuery = mainQuery + " AND  FUNNY_COUNT + USEFUL_COUNT + COOL_COUNT " + GUIManager.noOfVotesDropDown.getSelectedItem() + " " + GUIManager.noOfVotesValue.getText();
					}

					SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
						String spinnerFromValue = formater.format(GUIManager.fromdatespinner.getValue());
						String spinnerToValue = formater.format(GUIManager.todatespinner.getValue());
						mainQuery = mainQuery + " AND TO_DATE(REVIEWDATE,'YYYY-MM-DD') BETWEEN TO_DATE('" + spinnerFromValue.toString() + "','YYYY-MM-DD') AND TO_DATE('" + spinnerToValue.toString() + "','YYYY-MM-DD')";

					
					System.out.println(mainQuery);
					ResultSet rs = stmt.executeQuery(mainQuery);
					
					List<String> row;
					while(rs.next()) {
						row = new ArrayList<String>();
						for(int j = 1; j <= noOfFields; j++) {
							row.add(rs.getString(j));
						}
						reviewList.add(row);
					}
				} catch(Exception x) {
					System.out.println("get Checkin error " + x.getMessage());x.printStackTrace();
//					x.printStackTrace();
				}




				for(List<String> row : reviewList) {
					reviewDatatable.addRow(row.toArray());
				}
				
				JScrollPane sp = new JScrollPane(reviewTable);
				
				reviewResultPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1;
				c.weighty = 1;
				reviewResultPanel.add(sp,c);
				reviewFrame.add(reviewResultPanel,BorderLayout.CENTER);
				reviewFrame.setTitle("YELP USER  : ");
				reviewFrame.setSize(800,600);
				reviewFrame.setVisible(true);
				
				
			}
		};

		public static ActionListener checkInSearchBtnActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame checkinFrame = new JFrame();
				
				JPanel checkinResultPanel = new JPanel();
				
				String columns[] = {"Day","Hour","Count","Business_Id"};
				
				JTable checkinTable = new JTable();
				DefaultTableModel checkinDatatable = new DefaultTableModel();
				
				for(String columnName : columns)
					checkinDatatable.addColumn(columnName);
				checkinTable.setModel(checkinDatatable);
				
				List<List<String>> checkinList = new ArrayList<List<String>>();;


				try {
					if(DBManager.conn == null) DBManager.init();
					Statement stmt = DBManager.conn.createStatement();
					int noOfFields = 4;
					String mainQuery = "SELECT DAY, HOUR, COUNT, BID from business_checkin where 1=1";
					String whereCondition = " AND ";
					/*if(GUIManager.checkinSelectDropDown.getSelectedItem()=="OR")
					{
						whereCondition = " OR ";
					}*/
					if(GUIManager.fromdayComboBox.getSelectedItem()!="Select")
					{
						int day = 0;
						String d = "" + GUIManager.fromdayComboBox.getSelectedItem();
						switch(d)
						{
						case "Monday": day = 1;
									   break;
						case "Tuesday": day = 2;
									   break;
						case "Wednesday": day = 3;
						   			   break;
						case "Thrusday": day = 4;
									   break;
						case "Friday": day = 5;
									   break;
						case "Saturday": day = 6;
									   break;
						default: day = 0;
						}
						//System.out.println("dadadadad  day is"+day);
						mainQuery = mainQuery + whereCondition + " DAY  BETWEEN " + day;
					}

					if(GUIManager.fromdayComboBox.getSelectedItem()!="Select")
					{
						int tday = 0;
						String d = "" + GUIManager.todayComboBox.getSelectedItem();
						switch(d)
						{
						case "Monday": tday = 1;
									   break;
						case "Tuesday": tday = 2;
									   break;
						case "Wednesday": tday = 3;
						   			   break;
						case "Thrusday": tday = 4;
									   break;
						case "Friday": tday = 5;
									   break;
						case "Saturday": tday = 6;
									   break;
						default: tday = 0;
						}
						//System.out.println("dadadadad  day is"+ tday);
						mainQuery = mainQuery +" AND "+ tday;
					}
					
					if(GUIManager.fromtimeComboBox.getSelectedItem()!="Select")
					{
						mainQuery = mainQuery + whereCondition + " HOUR  BETWEEN " + GUIManager.fromtimeComboBox.getSelectedItem() + " " ;
					}


					if(GUIManager.totimeComboBox.getSelectedItem()!="Select")
					{
						mainQuery = mainQuery + " AND "  + GUIManager.totimeComboBox.getSelectedItem() + " ";
					}
					System.out.println("this is "+GUIManager.valueTextField.getText());
					if(GUIManager.numCheckinComboBox.getSelectedItem()!="Any" &&!GUIManager.valueTextField.getText().toString().equals(""))
					{
						mainQuery = mainQuery + whereCondition + " COUNT " + GUIManager.numCheckinComboBox.getSelectedItem() + " " + GUIManager.valueTextField.getText();
					}
					
					System.out.println(mainQuery);
					ResultSet rs = stmt.executeQuery(mainQuery);
					
					List<String> row;
					while(rs.next()) {
						row = new ArrayList<String>();
						for(int j = 1; j <= noOfFields; j++) {
							row.add(rs.getString(j));
						}
						checkinList.add(row);
					}
				} catch(Exception x) {
					System.out.println("get Checkin error " + x);
				}




				for(List<String> row : checkinList) {
					checkinDatatable.addRow(row.toArray());
				}
				
				JScrollPane sp = new JScrollPane(checkinTable);
				
				checkinResultPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1;
				c.weighty = 1;
				checkinResultPanel.add(sp,c);
				checkinFrame.add(checkinResultPanel,BorderLayout.CENTER);
				checkinFrame.setTitle("Checkins : ");
				checkinFrame.setSize(800,600);
				checkinFrame.setVisible(true);
				
				
			}
		};

		
		public static MouseAdapter tableMouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
		        JTable table =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2) {
		        	String bid = (String)GUIManager.resultDatatable.getValueAt(row, 1);
		        	System.out.println("Showing Review for " + bid);
		        	GUIManager.showReviewFrame(bid);
		        }
		    }
		};

		public static MouseAdapter userTableMouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
		        JTable table =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2) {
		        	String userid = (String)GUIManager.userDatatable.getValueAt(row, 1);
		        	System.out.println("Showing Review for " + userid);
		        	GUIManager.showReviewFrameForUser(userid);
		        }
		    }
		};
		
		
	}
	
	public static class DBManager {
		static Connection conn;
		
		//Database Credentials
		static String URL = "jdbc:oracle:thin:@localhost:1521/XE";
		static String USER = "sandesh";
		static String PASSWORD = "system";
		
		public static void init() {
			System.out.println("Initializing Database");
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("Connecting with " + URL);
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				System.out.println("Connection Successful");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		public static List<Map<String,Object>> executeQuery(String query){
			List<Map<String,Object>> resultSet = new ArrayList<Map<String,Object>>();
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					System.out.println(rs.getString(2));
					rs.next();
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			return resultSet;
		}
		
		public static List<String> getAllMainCategories() {
			List<String> mainCategories = new ArrayList<String>();
			try {
				if(conn == null) init();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT Unique(cname) FROM BUSINESS_MAIN_CATEGORY ORDER BY cname");
				
				while(rs.next()) {
					mainCategories.add(rs.getString(1));
				}
			} catch(Exception e) {
				System.out.println("Error " + e);
				e.printStackTrace();
			}
			return mainCategories;
		}
		
		public static List<String> getAllSubCategories(List<String> mainCategories, String construct) {
			List<String> subCategories = new ArrayList<String>();
			if(mainCategories.isEmpty()) return subCategories;
			try {
				if(conn == null) init();
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
							
				//Final Query
				//"cname"
				String sqlQuery = "SELECT UNIQUE(CNAME) FROM BUSINESS_SUB_CATEGORY WHERE " +String.join(" AND ", conditions) + " ORDER BY CNAME";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					subCategories.add(rs.getString(1));
				}
			} catch(Exception e) {
				System.out.println("Error " + e);
				e.printStackTrace();
			}
			return subCategories;
		}
		
		public static List<String> getAllAttributes(List<String> mainCategories, List<String> subCategories, String construct) {
			List<String> attributes = new ArrayList<String>();
			if(mainCategories.isEmpty() || subCategories.isEmpty()) return attributes;
			try {
				if(conn == null) init();
				//SELECT unique(attribute) FROM BUSINESS_ATTRIBUTE 
				//WHERE BID IN 
				//			(SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE cname IN ('Restaurants'))
				//			AND
				//		BID IN
				//			(SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE cname IN ('Car Wash'));
				
				//Prepare Query for Main Categories - OR Construct
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
				
				//SubCategories Optional with AND condition other condition
				if(subCategories != null && subCategories.size() > 0) {
					int countSubCategories = subCategories.size();
					pHolder.clear();
					while(pHolder.size() != countSubCategories) pHolder.add("?");
					
					String subQuerySubCat = "SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID"; 
					} else {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countSubCategories;
					}
					conditions.add("(BID IN (" + subQuerySubCat + "))");
				}
				
				//Final Query
				//"Attribute"
				String sqlQuery = "SELECT UNIQUE(Attribute) FROM BUSINESS_ATTRIBUTE WHERE " +String.join(" AND ", conditions) + " ORDER BY Attribute";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				if(subCategories != null) {
					for(String cat : subCategories) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					attributes.add(rs.getString(1));
				}
			} catch(Exception e) {
				System.out.println("Error " + e);
				e.printStackTrace();
			}
			return attributes;
		}
		
		public static List<String> getAllLocations(List<String> mainCategories, List<String> subCategories, List<String> attributes, String construct){
			List<String> locations = new ArrayList<String>();
			if(mainCategories.isEmpty()) return locations;
			try {
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
				
				//SubCategories Optional with AND condition other condition
				if(subCategories != null && subCategories.size() > 0) {
					int countSubCategories = subCategories.size();
					pHolder.clear();
					while(pHolder.size() != countSubCategories) pHolder.add("?");
					
					String subQuerySubCat = "SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID"; 
					} else {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countSubCategories;
					}
					conditions.add("(BID IN (" + subQuerySubCat + "))");
				}
				
				//Attribute - Optional with AND condition other condition
				if(attributes != null && attributes.size() > 0) {
					int countAttributeCategories = attributes.size();
					pHolder.clear();
					while(pHolder.size() != countAttributeCategories) pHolder.add("?");
					
					String subQueryAttributeCat = "SELECT BID FROM BUSINESS_ATTRIBUTE WHERE ATTRIBUTE IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID"; 
					} else {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID HAVING COUNT(ATTRIBUTE) = " + countAttributeCategories;
					}
					conditions.add("(BID IN (" + subQueryAttributeCat + "))");
				}
				
				//Final Query
				//"Business Id","Address","City","State","Stars","# of Reviews","# of Checkins"
				String sqlQuery = "SELECT UNIQUE(CITY), STATE FROM BUSINESS WHERE " +String.join(" AND ", conditions) + " ORDER BY STATE";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				if(subCategories != null) {
					for(String cat : subCategories) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				if(attributes != null) {
					for(String cat : attributes) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					String location = rs.getString(1) + "," + rs.getString(2);
					locations.add(location);
				}
	 			
			} catch (Exception e) {
				System.out.println("Get Location Error " + e);
			}
			
			return locations;
		}
		
		public static Map<Integer, Map<String, Integer>> getAllDays(List<String> mainCategories, List<String> subCategories, List<String> attributes, String location, String construct){
			Map<Integer, Map<String, Integer>> daysMap = new HashMap<Integer, Map<String, Integer>>();
			if(mainCategories.isEmpty()) return daysMap;
			try {
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
				
				//SubCategories Optional with AND condition other condition
				if(subCategories != null && subCategories.size() > 0) {
					int countSubCategories = subCategories.size();
					pHolder.clear();
					while(pHolder.size() != countSubCategories) pHolder.add("?");
					
					String subQuerySubCat = "SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID"; 
					} else {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countSubCategories;
					}
					conditions.add("(BID IN (" + subQuerySubCat + "))");
				}
				
				//Attribute - Optional with AND condition other condition
				if(attributes != null && attributes.size() > 0) {
					int countAttributeCategories = attributes.size();
					pHolder.clear();
					while(pHolder.size() != countAttributeCategories) pHolder.add("?");
					
					String subQueryAttributeCat = "SELECT BID FROM BUSINESS_ATTRIBUTE WHERE ATTRIBUTE IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID"; 
					} else {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID HAVING COUNT(ATTRIBUTE) = " + countAttributeCategories;
					}
					conditions.add("(BID IN (" + subQueryAttributeCat + "))");
				}
				
				//Location
				String state = null;
				String city = null;
				if(location != null) {
					String[] locArr = location.split(",");
					state = locArr[1];
					city = locArr[0];
					conditions.add("(BID IN (SELECT BID FROM BUSINESS WHERE STATE = ? AND CITY = ?))");
				}
				//Final Query
				String sqlQuery = "SELECT Day, MIN(OPEN), MAX(CLOSE) FROM BUSINESS_HOUR WHERE " +String.join(" AND ", conditions) + " GROUP BY DAY ORDER BY DAY";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				if(subCategories != null) {
					for(String cat : subCategories) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				if(attributes != null) {
					for(String cat : attributes) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				
				if(location != null) {
					pstmt.setString(paramIndex, state);
					paramIndex++;
					pstmt.setString(paramIndex, city);
					paramIndex++;
				}
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					int d = rs.getInt(1);
					if(d < 0 || d > 6)
						continue;
					
					Map<String, Integer> tempTiming = new HashMap<String, Integer>();
					tempTiming.put("from",rs.getInt(2));
					tempTiming.put("to",rs.getInt(3));
					daysMap.put(d,tempTiming);
					
				}
	 			
			} catch (Exception e) {
				System.out.println("Get All Days Error " + e);
			}
			
			return daysMap;
		}
		
		public static List<List<String>> getAllBusiness(List<String> mainCategories, List<String> subCategories, List<String> attributes, String location, Integer day, Integer openingHour, Integer closingHour, String construct){
			List<List<String>> businessList = new ArrayList<List<String>>();
			if(mainCategories.isEmpty()) return businessList;
			try {
				List<String> pHolder = new ArrayList<String>();
				
				List<String> conditions = new ArrayList<String>();
				
				//Form query mainCategories - Mandatory
				int countMainCategories = mainCategories.size();
				pHolder.clear();
				while(pHolder.size() != countMainCategories) pHolder.add("?");
				
				String subQueryMainCat = "SELECT BID FROM BUSINESS_MAIN_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
				if(construct.equalsIgnoreCase("or")) {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID"; 
				} else {
					subQueryMainCat = subQueryMainCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countMainCategories;
				}
				conditions.add("(BID IN (" + subQueryMainCat + "))");
				
				//SubCategories Optional with AND condition other condition
				if(subCategories != null && subCategories.size() > 0) {
					int countSubCategories = subCategories.size();
					pHolder.clear();
					while(pHolder.size() != countSubCategories) pHolder.add("?");
					
					String subQuerySubCat = "SELECT BID FROM BUSINESS_SUB_CATEGORY WHERE CNAME IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID"; 
					} else {
						subQuerySubCat = subQuerySubCat + " GROUP BY BID HAVING COUNT(CNAME) = " + countSubCategories;
					}
					conditions.add("(BID IN (" + subQuerySubCat + "))");
				}
				
				//Attribute - Optional with AND condition other condition
				if(attributes != null && attributes.size() > 0) {
					int countAttributeCategories = attributes.size();
					pHolder.clear();
					while(pHolder.size() != countAttributeCategories) pHolder.add("?");
					
					String subQueryAttributeCat = "SELECT BID FROM BUSINESS_ATTRIBUTE WHERE ATTRIBUTE IN (" + String.join(",", pHolder) + ")";
					if(construct.equalsIgnoreCase("or")) {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID"; 
					} else {
						subQueryAttributeCat = subQueryAttributeCat + " GROUP BY BID HAVING COUNT(ATTRIBUTE) = " + countAttributeCategories;
					}
					conditions.add("(BID IN (" + subQueryAttributeCat + "))");
				}
				
				//Location
				String state = null;
				String city = null;
				if(location != null) {
					String[] locArr = location.split(",");
					state = locArr[1];
					city = locArr[0];
					conditions.add("(STATE = ? AND CITY = ?)");
				}
				String checkInCountQuery = "SELECT SUM(COUNT) FROM BUSINESS_CHECKIN WHERE BUSINESS.BID = BUSINESS_CHECKIN.BID";
				if(day != null) {
					int from = 0;
					int to = 24;
					if(openingHour==null) openingHour = 24*60;
					else from = Math.floorDiv(openingHour, 60);
					if(closingHour==null) closingHour = 0;
					else to = Math.floorDiv(closingHour, 60);
					conditions.add("(0 < (SELECT COUNT(BID) FROM BUSINESS_HOUR WHERE BUSINESS_HOUR.BID = BUSINESS.BID AND DAY = "+day+" AND OPEN <= " + openingHour + " AND CLOSE >= " + closingHour + "))");
					checkInCountQuery = checkInCountQuery + " AND DAY = " + day + " AND HOUR >= " + from + " AND HOUR <= " + to;
					
				}
				
				//Final Query
				//"Business Id","Name","Address","City","State","Stars","# of Reviews","# of Checkins"
				int noOfFields = 8;
				String sqlQuery = "SELECT BID, NAME, ADDRESS, CITY, STATE, REVIEW_COUNT, STARS, ("+checkInCountQuery+") FROM BUSINESS WHERE " +String.join(" AND ", conditions) + " ORDER BY NAME";
				System.out.println("SQL QUERY : " + sqlQuery);
				PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
				int paramIndex = 1;
				for(String cat : mainCategories) {
					pstmt.setString(paramIndex, cat);
					paramIndex++;
				}
				if(subCategories != null) {
					for(String cat : subCategories) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				if(attributes != null) {
					for(String cat : attributes) {
						pstmt.setString(paramIndex, cat);
						paramIndex++;
					}
				}
				
				if(location != null) {
					pstmt.setString(paramIndex, state);
					paramIndex++;
					pstmt.setString(paramIndex, city);
					paramIndex++;
				}
				
				
				ResultSet rs = pstmt.executeQuery();
				int i=1;
				List<String> row;
				while(rs.next()) {
					row = new ArrayList<String>();
					row.add(String.valueOf(i));
					for(int j = 1; j <= noOfFields; j++) {
						if(j==noOfFields) {
							if(rs.getString(j) == null) row.add("0");
							else row.add(rs.getString(j));
						} else {
							row.add(rs.getString(j));
						}
					}
					i++;
					businessList.add(row);
				}
	 			
			} catch (Exception e) {
				System.out.println("Get All Business Error " + e);
			}
			
			return businessList;
		}
		
		public static List<List<String>> getReviews(String bid) {
			List<List<String>> reviewList = new ArrayList<List<String>>();
			try {
				Statement stmt = conn.createStatement();
				int noOfFields = 7;
				ResultSet rs = stmt.executeQuery("SELECT ReviewDate, Stars, ReviewText, (SELECT NAME FROM Yelp_user WHERE YELP_USER.USERID = YELP_REVIEW.USERID), USEFUL_COUNT, FUNNY_COUNT, COOL_COUNT from yelp_review WHERE BID = '" + bid + "' ORDER BY REVIEWDATE");
				
				List<String> row;
				while(rs.next()) {
					row = new ArrayList<String>();
					for(int j = 1; j <= noOfFields; j++) {
						row.add(rs.getString(j));
					}
					reviewList.add(row);
				}
			} catch(Exception e) {
				System.out.println("get Review error " + e);
			}
			return reviewList;
		}
		
		public static String escape(String value) {
			return value.replaceAll("'", "\\\\'");
		}
		
		/*public static void main(String args[]) {
			//Test Method for DBManager
			//init();
			//executeQuery("Select Ename From EMP");
			System.out.println(escape("apple's"));
		}*/
	}

}



 
class DateLabelFormatter extends AbstractFormatter {
 
    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
     
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }
 
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
         
        return "";
    }
 
}
