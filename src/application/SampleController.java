package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Callback;





public class SampleController {
	/*_______________________________-FXML ATTRIBUTES-________________________________________________*/
	
    @FXML
    private AnchorPane anchor;

    @FXML
    private ComboBox<String> CB_from;

    @FXML
    private ComboBox<String> CB_to;

    @FXML
    private TextArea TA_path;

    @FXML
    private TextField TF_distance;
    
    @FXML
    private JFXButton btn_reset;

    @FXML
    private JFXButton btn_find;

    @FXML
    private JFXButton btn_table;

    

	
	/*_______________________________-NONFXML ATTRIBUTES-___________________________________________*/
    
    int numStatesSelected = 0;
    
    ArrayList<Vertex> V;
    ArrayList<String> S;
    ArrayList<CheckBox> R;
    HashMap<String, Vertex> M = new HashMap<String, Vertex>();
    Set<Integer> modified = new HashSet<Integer>();
    ArrayList<TableEntry> T = new ArrayList<TableEntry>();
    CheckBox c1 = new CheckBox();
    CheckBox c2 = new CheckBox();
    ArrayList<Object> Lines = new ArrayList<Object>();
    double totalDistance = 0;
    double xToKm = 5000.0/1600.0;
    double yToKm = 2800.0/800.0;
    int flag = 0;
	
    
    /*________________________________-FXML METHODS-_________________________________________________*/
    
    @FXML
    void Find(ActionEvent event) {
    	
    	Vertex from = (Vertex) M.get(CB_from.getEditor().getText());
    	Vertex to = (Vertex) M.get(CB_to.getEditor().getText());
    	
    	modified.add(from.getNum());
    	modified.add(to.getNum());
    	
    	if(to == null)System.out.println("am null");
    	if(from == null)System.out.println("am null");
    	
    	if(from.getName().equals(to.getName()))flag = 2;
    	else {
    	
		//initialize source state 
		T.get(from.getNum()).distance=0;

	
		//make priority queue
		PriorityQueue<HeapEntry> heap = new PriorityQueue<HeapEntry>();
		heap.add(new HeapEntry(0, from.getNum()));
		
		HeapEntry currentHeapEntry;
		Vertex current;
		Edge adjacent;
		double newDist=0.0;
		
		//Dijikstra
		while(heap.isEmpty() == false)	{ 
			currentHeapEntry = heap.remove();
			
			if(T.get(currentHeapEntry.getToEdge()).known) //(currentHeapEntry.getDistance() > T.get(currentHeapEntry.getToEdge()).distance) 
				continue;
			
			current = V.get(currentHeapEntry.getToEdge());
			T.get(current.getNum()).known = true;
			if(current == to)break;
			
			adjacent = current.getNext();
			
			while(adjacent != null) { 
				if(T.get(adjacent.getNum()).known == false) {
				
				newDist = T.get(current.getNum()).distance + adjacent.getWeight();
				if(newDist < T.get(adjacent.getNum()).distance) {
					modified.add(adjacent.getNum());
					T.get(adjacent.getNum()).distance = newDist;
					T.get(adjacent.getNum()).path = current.getNum();
					heap.add(new HeapEntry(newDist, adjacent.getNum())); 
				}
				}
				adjacent = adjacent.getNext();
			}
			
		}
		
		
		//if theres no path
		if(T.get(to.getNum()).known == false) {
			flag = 1;
			TF_distance.setText("No path found! ");	
			
		}
		else {
		//Draw lines
		int prev = to.getNum();
		int v = T.get(to.getNum()).path;
		double x1,x2,y1,y2,deltaX,deltaY;
		do {
			x1 = R.get(prev).getLayoutX();
			y1 = R.get(prev).getLayoutY();
			x2 = R.get(v).getLayoutX();
			y2 = R.get(v).getLayoutY();
			
			deltaX = x2-x1;
			deltaY = y2-y1;
			
			totalDistance += Math.sqrt(Math.pow(deltaX*xToKm, 2) + Math.pow(deltaY*yToKm, 2));
			
			Line line = new Line(x1,y1,x2,y2);
			line.setStrokeWidth(3);
			
			Circle circle = new Circle(8);
			circle.setLayoutX(x1);
			circle.setLayoutY(y1);
			
			Circle circle2 = new Circle(4);
			circle2.setLayoutX(x1);
			circle2.setLayoutY(y1);
			circle2.setFill(Color.RED);
			
			anchor.getChildren().addAll(line,circle,circle2);
			
			Lines.add(line);
			Lines.add(circle);
			Lines.add(circle2);
			
			
			
			
			prev = v;   
			v = T.get(v).path;
		}while(v != 0);
		
		Circle circle = new Circle(16);
		circle.setLayoutX(R.get(to.getNum()).getLayoutX());
		circle.setLayoutY(R.get(to.getNum()).getLayoutY());
		
		Circle circle4 = new Circle(8);
		circle4.setLayoutX(R.get(to.getNum()).getLayoutX());
		circle4.setLayoutY(R.get(to.getNum()).getLayoutY());
		circle4.setFill(Color.RED);
		
		Circle circle2 = new Circle(16);
		circle2.setLayoutX(R.get(from.getNum()).getLayoutX());
		circle2.setLayoutY(R.get(from.getNum()).getLayoutY());
		
		
		Circle circle3 = new Circle(8);
		circle3.setLayoutX(R.get(from.getNum()).getLayoutX());
		circle3.setLayoutY(R.get(from.getNum()).getLayoutY());
		circle3.setFill(Color.RED);
		
		anchor.getChildren().addAll(circle2,circle,circle3,circle4);
		
		Lines.add(circle2);
		Lines.add(circle);
		Lines.add(circle3);
		Lines.add(circle4);
		

		
		//show distance
		TF_distance.setText(String.format("%.2f", totalDistance) + " Km");

		//show path
		TA_path.setWrapText(true);
		String path = findPath(T, to, "");
		TA_path.setText(path.substring(0,path.length()-3));
		
		//print table
		for (int i = 1; i < T.size(); i++) {
				System.out.println(i + " " + T.get(i).known + " " + T.get(i).distance + " " + T.get(i).path);
		}
		}
    	}
		if(flag == 1) {
			btn_reset.fire();
			TF_distance.setText("No path found! ");
			
		}
		else if(flag == 2) {
			btn_reset.fire();
			TF_distance.setText("0.0Km");
		}
    	flag = 0;
    }

    
    
    
    @FXML
    void Reset(ActionEvent event) {
    	numStatesSelected = 0;
    	totalDistance = 0.0;
    	
    	c1.setSelected(false);
    	c2.setSelected(false);
    	
    	CB_from.getSelectionModel().clearSelection();
    	CB_to.getSelectionModel().clearSelection();
    	CB_from.getEditor().setText("");
    	CB_to.getEditor().setText("");
    	
    	TF_distance.setText("");
    	TA_path.setText("");
    	
		for (int index : modified) {
	        T.get(index).distance = Integer.MAX_VALUE;
	        T.get(index).known = false;
	        T.get(index).path = 0;
	     }
	
		//remove lines
		anchor.getChildren().removeAll(Lines);
		
    }
    
    
    
    @FXML
    void ShowTable(ActionEvent event) {
    	Stage stage = new Stage();
 	   TextArea ta = new TextArea();
 	   ta.setStyle("-fx-font-size: 2em;");
 	   StackPane pane = new StackPane();
 	   pane.getChildren().add(ta);
 	   Scene scene = new Scene(pane,1000,600);
 	   ta.setText(String.format("%-25s | %-25s | %-25s | %-25s \n","index","Visited","Distance","Path"));
 	  ta.setText(ta.getText() +"__________________________________________________________________________________\n");
 	  for (int i = 1; i < T.size(); i++) {
 		 ta.setText(ta.getText() + String.format("%-25d | %-25s | %-25.2f | %-25d \n",i,T.get(i).known,T.get(i).distance,T.get(i).path));
	}
 	   
 	   stage.setScene(scene);
 	   stage.show();
    }
    
    
    /*______________________________-NONFXML METHODS-________________________________________________*/
    
    public String findPath(ArrayList<TableEntry> T, Vertex v, String path) {
    	if(T.get(v.getNum()).path != 0) {
    		path = findPath(T, V.get(T.get(v.getNum()).path), path);
    	}
    	path += v.getName() + " -> ";
    	return path;
    	
    }
    
    
   
    
	
    public void initialize() throws FileNotFoundException {
    	File file =  new File("E:\\3rdYear1\\Algorithms\\Cities.txt"); 
    	Scanner sc = new Scanner(file); 
    
    	int numV = sc.nextInt();
    	int numE = sc.nextInt();
    	
    	//make arraylists 
    	V = new ArrayList<Vertex>(); //Vertex
    	V.add(new Vertex(""));
    	S = new ArrayList<String>(); //Names
    	S.add("");
    	R = new ArrayList<CheckBox>();//check boxes
    	R.add(new CheckBox());
    	CheckBox c;
    	
    	int index,x,y;
    	String name;
    	
    	//adding check boxes, and filling arraylists
    	Vertex v = null;
    	for (int i = 0; i < numV; i++) {
    		index = sc.nextInt();
    		sc.useDelimiter(":");
    		name = sc.next().trim();
    		sc.reset();
    		sc.next();
    		x = sc.nextInt();
    		y = sc.nextInt();
    		
    		v = new Vertex(index, name);
			V.add(v);
			S.add(name);
			M.put(name, v);
			
			c = new CheckBox(name.trim());
			c.setStyle("-fx-font-size: 14; -fx-text-wrap: true; -fx-font-weight: bold;");
			anchor.getChildren().add(c);
	    	c.setLayoutX(x);
	    	c.setLayoutY(y);
	    	R.add(c);
		}
 
    	//Add edges as linked lists
    	int from, to;
    	double distance,x1,y1,x2,y2;
    	Edge newEdge,current;
    	for (int i = 0; i < numE; i++) {
			from = sc.nextInt();
			to = sc.nextInt();
			
			x1 = R.get(from).getLayoutX();
			y1 = R.get(from).getLayoutY(); 
			x2 = R.get(to).getLayoutX();
			y2 = R.get(to).getLayoutY();
			
			
			distance = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
			newEdge = new Edge(to,distance);
			
			//add edge to vertex
			Vertex currentV = V.get(from);
			if(currentV.getNext() == null)currentV.setNext(newEdge); // if no edges
			else {													 
				current = currentV.getNext();
				while(current.getNext() != null)current = current.getNext(); //skip all edges
				current.setNext(newEdge);
			}
			

		}
    	

       	sc.close();
       	
       	
       	
       	
    	//combobox of cities from
        CB_from.setEditable(true);

        // Create a list .
        ObservableList<String> items = FXCollections.observableArrayList(S);

        // Create a FilteredList wrapping the ObservableList.
        FilteredList<String> filteredItems = new FilteredList<String>(items, p -> true);

        // Add a listener to the textProperty of the combobox editor. The
        // listener will simply filter the list every time the input is changed
        // as long as the user hasn't selected an item in the list.
        CB_from.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = CB_from.getEditor();
            final String selected = CB_from.getSelectionModel().getSelectedItem();

            Platform.runLater(() -> {
                // If the no item in the list is selected or the selected item
                // isn't equal to the current input, we refilter the list.
                if (selected == null || !selected.equals(editor.getText())) { //tostr
                    filteredItems.setPredicate(item -> {
                        // We return true for any items that starts with the
                        // same letters as the input. We use toUpperCase to
                        if (item.toUpperCase().trim().startsWith(newValue.toUpperCase())) { 
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });

        CB_from.setItems(filteredItems);
        CB_from.setCellFactory(new Callback<ListView<String>,ListCell<String>>(){

            @Override
            public ListCell<String> call(ListView<String> p) {
                
                final ListCell<String> cell = new ListCell<String>(){

                    @Override
                    protected void updateItem(String t, boolean bln) {
                        super.updateItem(t, bln);
                        
                        if(t != null){
                            setText(t);
                        }else{
                            setText(null);
                        }
                    }
 
                };
                
                return cell;
            }
        });

        
        
        
        
        
        
        
        
        
        //Combobox To      
        CB_to.setEditable(true);
        FilteredList<String> filteredItemsTo = new FilteredList<String>(items, p -> true);

        CB_to.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = CB_to.getEditor();
            final String selected = CB_to.getSelectionModel().getSelectedItem();

            Platform.runLater(() -> { 

                if (selected == null || !selected.equals(editor.getText())) { //tostr
                    filteredItemsTo.setPredicate(item -> {

                        if (item.toUpperCase().trim().startsWith(newValue.toUpperCase())) { 
                            return true;
                        } else {
                            return false; 
                        }
                    });
                }
            });
        });

        CB_to.setItems(filteredItemsTo);
        CB_to.setCellFactory(new Callback<ListView<String>,ListCell<String>>(){

            @Override
            public ListCell<String> call(ListView<String> p) {
                
                final ListCell<String> cell = new ListCell<String>(){

                    @Override
                    protected void updateItem(String t, boolean bln) {
                        super.updateItem(t, bln);
                        
                        if(t != null){
                            setText(t);
                        }else{
                            setText(null);
                        }
                    }
 
                };
                
                return cell;
            }
        });
       
      
        
        
        
        
        

        EventHandler eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	CheckBox pressed = (CheckBox)event.getSource();
                System.out.println(R.indexOf(pressed));
                
                 if(pressed.isSelected()) {
                	 if(numStatesSelected == 0) {
                		 c1 = pressed;
                		 numStatesSelected++;
                		 CB_from.getEditor().setText(pressed.getText()); 
                		 }
                	 else if(numStatesSelected == 1) {
                		 c2 = pressed;
                		 numStatesSelected++;
                		 CB_to.getEditor().setText(pressed.getText());
                	 }
                	 else pressed.setSelected(false); 
                	 
                 }
                 else {
                	 numStatesSelected--;
                	 
                 }
            }
        };
        
        //checkbox tick
        for (int i = 0; i < R.size(); i++) {
			R.get(i).setOnAction(eh);
		}
        
        
        T.add(new TableEntry());
        for (int i = 1; i < V.size(); i++) {
			T.add(new TableEntry());
			T.get(i).distance=Integer.MAX_VALUE;
		}
        
    }
    
 
   
    
        }

		