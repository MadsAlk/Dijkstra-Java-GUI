package application;

public class Vertex {
private int num;	
private String name;
private Edge next;

public Vertex(int num, String name) {
	super();
	this.num = num;
	this.name = name;
}

public int getNum() {
	return num;
}

public void setNum(int num) {
	this.num = num;
}



public Edge getNext() {
	return next;
}

public void setNext(Edge next) {
	this.next = next;
}

public Vertex(String name) {
	super();
	this.name = name;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

@Override
public String toString() {
	return getName();
}
}
