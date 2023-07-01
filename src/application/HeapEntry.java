package application;

public class HeapEntry implements Comparable<HeapEntry> {
private double distance;
private int toEdge;

public HeapEntry(double distance, int toEdge) {
	super();
	this.distance = distance;
	this.toEdge = toEdge;
}
public double getDistance() {
	return distance;
}
public void setDistance(double distance) {
	this.distance = distance;
}
public int getToEdge() {
	return toEdge;
}
public void setToEdge(int toEdge) {
	this.toEdge = toEdge;
}

public boolean equals(HeapEntry other){
	return this.getDistance() == other.getDistance();
}

public int compareTo(HeapEntry other) {
if(this.equals(other))return 0;
else if(this.getDistance() > other.getDistance())return 1;
else return -1;
}




}
