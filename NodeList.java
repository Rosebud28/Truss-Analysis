import java.util.ArrayList;

public class NodeList
{
  private ArrayList<Node> nodeArray;

  public NodeList()
  {
    this.nodeArray = new ArrayList<Node>();
  }

  public void addNode(Node node) {
    if (this.nodeArray.size() == 0) {
      this.nodeArray.add(node);
    }
    else {
      boolean contains = false;
      for (int i = 0; i < this.nodeArray.size(); i++) {
        if (((Node)this.nodeArray.get(i)).contains(node.getLocation())) {
          contains = true;
        }
      }
      if (!contains)
        this.nodeArray.add(node);
    }
  }

  public void removeNode(int i)
  {
    this.nodeArray.remove(i);
  }
  

  public Node getNode(int i) {
    return (Node)this.nodeArray.get(i);
  }

  public int size() {
    return this.nodeArray.size();
  }
}