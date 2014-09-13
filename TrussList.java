import java.util.ArrayList;

public class TrussList
{
  private ArrayList<Truss> trussArray;

  public TrussList()
  {
    this.trussArray = new ArrayList<Truss>();
  }

  public boolean addTruss(Truss truss) {
    if (this.trussArray.size() == 0) {
      this.trussArray.add(truss);
      return true;
    }

    boolean contains = false;
    for (int i = 0; i < this.trussArray.size(); i++) {
      if (((Truss)this.trussArray.get(i)).equals(truss)) {
        contains = true;
      }
    }
    if (!contains) {
      this.trussArray.add(truss);
      return true;
    }
    return false;
  }

  public Truss getTruss(int i)
  {
    return (Truss)this.trussArray.get(i);
  }

  public int size() {
    return this.trussArray.size();
  }

  public void removeTruss(Truss truss) {
    for (int i = 0; i < this.trussArray.size(); i++)
      if (truss.equals(this.trussArray.get(i)))
        this.trussArray.remove(i);
  }

  public void removeTruss(ArrayList<Truss> trussArray)
  {
    for (int i = 0; i < this.trussArray.size(); i++)
      for (int j = 0; j < trussArray.size(); j++)
        if (((Truss)this.trussArray.get(i)).equals(trussArray.get(j)))
          this.trussArray.remove(i);
  }

  public void removeTruss(Node node1, Node node2)
  {
    Truss tempTruss = new Truss(node1, node2);
    for (int i = 0; i < this.trussArray.size(); i++)
      if (tempTruss.equals(this.trussArray.get(i))) {
        node1.removeTruss(tempTruss);
        node2.removeTruss(tempTruss);
        this.trussArray.remove(i);
      }
  }
}