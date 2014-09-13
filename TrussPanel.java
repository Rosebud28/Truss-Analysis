import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.*;

public class TrussPanel extends JPanel {
	private TrussPanel.operations operation = null;
	private JPanel drawPanel;
	private double scaleFactor;
	private JMenuItem addNodes;
	private JMenuItem removeNodes;
	private JMenuItem addTruss;
	private JMenuItem removeTruss;
	private JMenuItem allowDragButton;
	private JMenuItem addLoads;
	private JMenuItem removeLoads;
	private JMenuItem addFixNode;
	private JMenuItem addRoller;
	private JButton drag;
	private JButton setCoordinate;
	private JButton calculate;
	private JButton clearAll;
	private JMenuBar menuBar;
	private JMenu add;
	private JMenu remove;
	private JLabel instruction;
	private JLabel instruction2;
	private JTextField input1;
	private JTextField input2;
	private JPanel southPanel;
	private JPanel infoPanel;
	private JPanel dragAndSelectPanel;
	private NodeList nodeList;
	private TrussList trussList;
	private boolean firstClick;
	private Node tempNode1;
	private Node tempNode2;
	private Node currentNode;
	private Node currentStaticNode;
	private boolean allowDrag;
	private Node origin;
	private FixNode fixNode; // design for just one fixnode and one roller.
	private Roller roller;

	public TrussPanel() {
		setPreferredSize(new Dimension(800, 400));
		setMinimumSize(new Dimension(800, 400));
		setMaximumSize(new Dimension(800, 400));
		setLayout(new BorderLayout());
		origin = new Node(new Point(200, 250), Color.GREEN);
		scaleFactor = 1;
		instruction = new JLabel("Click \"Add\" to Add Nodes");
		nodeList = new NodeList();
		nodeList.addNode(origin);
		trussList = new TrussList();
		firstClick = true;
		allowDrag = false;
		tempNode1 = null;
		tempNode2 = null;
		currentNode = null;
		menuBar = new JMenuBar();
		add = new JMenu("Add");
		remove = new JMenu("Remove");
		addNodes = new JMenuItem("Add Nodes");
		addNodes.addMouseListener(new AddNodes());
		removeNodes = new JMenuItem("Remove Nodes");
		removeNodes.addMouseListener(new TrussPanel.RemoveNodes());
		addTruss = new JMenuItem("Add Members");
		addTruss.addMouseListener(new TrussPanel.AddTrusses());
		removeTruss = new JMenuItem("Remove Members");
		removeTruss.addMouseListener(new TrussPanel.RemoveTrusses());
		allowDragButton = new JMenuItem("Drag Off");
		allowDragButton.addMouseListener(new TrussPanel.AllowDrag());
		addLoads = new JMenuItem("Add Loads");
		addLoads.addMouseListener(new TrussPanel.AddLoads());
		removeLoads = new JMenuItem("Remove Loads");
		removeLoads.addMouseListener(new TrussPanel.RemoveLoads());
		addFixNode = new JMenuItem("Set a Fix Node");
		addFixNode.addMouseListener(new AddFixNode());
		addRoller = new JMenuItem("Set a Roller");
		addRoller.addMouseListener(new AddRoller());
		add.add(addNodes);
		remove.add(removeNodes);
		add.add(addTruss);
		remove.add(removeTruss);
		add.add(addLoads);
		remove.add(removeLoads);
		add.add(addFixNode);
		add.add(addRoller);
		menuBar.add(add);
		menuBar.add(remove);
		add(menuBar, "North");
		drawPanel = new TrussPanel.DrawPanel();
		drawPanel.setBackground(Color.WHITE);
		drawPanel.addMouseListener(new TrussPanel.Click());
		drawPanel.addMouseMotionListener(new TrussPanel.Drag());
		drawPanel.addMouseListener(new TrussPanel.Release());
		instruction2 = new JLabel();
		input1 = new JTextField(10);
		input1.addActionListener(new Input());
		input2 = new JTextField(10);
		input2.addActionListener(new Input());
		southPanel = new JPanel();
		infoPanel = new JPanel();
		drag = new JButton("Drag Off");
		drag.addMouseListener(new AllowDrag());
		setCoordinate = new JButton("Set Coordinate");
		setCoordinate.addMouseListener(new Select());
		calculate = new JButton("Calculate");
		clearAll = new JButton("Clear All");
		calculate.addMouseListener(new Calculate());
		clearAll.addMouseListener(new ClearAll());
		infoPanel.add(instruction);
		dragAndSelectPanel = new JPanel();
		dragAndSelectPanel.add(drag);
		dragAndSelectPanel.add(setCoordinate);
		dragAndSelectPanel.add(calculate);
		dragAndSelectPanel.add(clearAll);
		southPanel.setLayout(new BorderLayout());
		southPanel.add(infoPanel, BorderLayout.WEST);
		southPanel.add(dragAndSelectPanel, BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);
		add(drawPanel);
	}

	private class Click extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (operation == TrussPanel.operations.AddNodes) {
				nodeList.addNode(new Node(e.getPoint()));
				instruction.setText("x: "
						+ (e.getPoint().x - origin.getLocation().x) / 10.0
						+ " y: " + (-e.getPoint().y + origin.getLocation().y)
						/ 10.0);
				TrussPanel.this.repaint();
			} else if (operation == TrussPanel.operations.RemoveNodes) {
				for (int i = 1; i <nodeList.size(); i++) {
					if (nodeList.getNode(i).contains(
							e.getPoint())) {
						int number = nodeList.getNode(i)
								.getTrussArray().size();
						for (int j = 0; j < number; j++) {
									nodeList
									.getNode(i)
									.getTheOtherNode(
											(Truss) nodeList
													.getNode(i).getTrussArray()
													.get(j))
									.removeTruss(
											(Truss) nodeList
													.getNode(i).getTrussArray()
													.get(j));
						}
						trussList
								.removeTruss(nodeList
										.getNode(i).getTrussArray());
						nodeList.removeNode(i);
					}
				}
				repaint();
			} else if (operation == TrussPanel.operations.AddTrusses) {
				if (firstClick) {
					for (int i = 1; i < nodeList.size(); i++) {
						if (nodeList.getNode(i).contains(
								e.getPoint())) {
							nodeList.getNode(i).setColor(
									Color.BLUE);
							tempNode1 = TrussPanel.this.nodeList
									.getNode(i);
							firstClick = false;
							repaint();
						}
					}
				} else {
					for (int i = 1; i < TrussPanel.this.nodeList.size(); i++) {
						if (TrussPanel.this.nodeList.getNode(i).contains(
								e.getPoint())) {
							TrussPanel.this.nodeList.getNode(i).setColor(
									Color.BLUE);
							TrussPanel.this.tempNode2 = TrussPanel.this.nodeList
									.getNode(i);
							if (TrussPanel.this.tempNode2 != TrussPanel.this.tempNode1) {
								TrussPanel.this.firstClick = true;
								TrussPanel.this.tempNode1.setColor(Color.BLACK);
								TrussPanel.this.tempNode2.setColor(Color.BLACK);
								if (TrussPanel.this.trussList
										.addTruss(new Truss(
												TrussPanel.this.tempNode1,
												TrussPanel.this.tempNode2))) {
									TrussPanel.this.tempNode1
											.addTruss(TrussPanel.this.trussList
													.getTruss(TrussPanel.this.trussList
															.size() - 1));
									TrussPanel.this.tempNode2
											.addTruss(TrussPanel.this.trussList
													.getTruss(TrussPanel.this.trussList
															.size() - 1));
								}
								TrussPanel.this.repaint();
							}
						}
					}
				}
			} else if (TrussPanel.this.operation == TrussPanel.operations.RemoveTrusses) {
				if (TrussPanel.this.firstClick) {
					for (int i = 1; i < TrussPanel.this.nodeList.size(); i++) {
						if (TrussPanel.this.nodeList.getNode(i).contains(
								e.getPoint())) {
							TrussPanel.this.nodeList.getNode(i).setColor(
									Color.BLUE);
							TrussPanel.this.tempNode1 = TrussPanel.this.nodeList
									.getNode(i);
							TrussPanel.this.firstClick = false;
							TrussPanel.this.repaint();
						}
					}
				} else {
					for (int i = 1; i < TrussPanel.this.nodeList.size(); i++) {
						if (TrussPanel.this.nodeList.getNode(i).contains(
								e.getPoint())) {
							TrussPanel.this.nodeList.getNode(i).setColor(
									Color.BLUE);
							TrussPanel.this.tempNode2 = TrussPanel.this.nodeList
									.getNode(i);
							TrussPanel.this.firstClick = true;
							TrussPanel.this.tempNode1.setColor(Color.BLACK);
							TrussPanel.this.tempNode2.setColor(Color.BLACK);
							Truss tempTruss = new Truss(
									TrussPanel.this.tempNode1,
									TrussPanel.this.tempNode2);
							if (TrussPanel.this.trussList.size() != 0) {
								TrussPanel.this.tempNode1
										.removeTruss(tempTruss);
								TrussPanel.this.tempNode2
										.removeTruss(tempTruss);
								TrussPanel.this.trussList
										.removeTruss(new Truss(
												TrussPanel.this.tempNode1,
												TrussPanel.this.tempNode2));
							}
							TrussPanel.this.repaint();
						}
					}
				}
			} else if (TrussPanel.this.operation == TrussPanel.operations.RemoveLoads) {
				for (int i = 1; i < TrussPanel.this.nodeList.size(); i++) {
					if (TrussPanel.this.nodeList.getNode(i).contains(
							e.getPoint())) {
						TrussPanel.this.nodeList.getNode(i).setLoad(null);
					}
				}
				TrussPanel.this.repaint();
			} else if (operation == operations.Select) {
				for (int i = 1; i < TrussPanel.this.nodeList.size(); i++) {
					if (TrussPanel.this.nodeList.getNode(i).contains(
							e.getPoint())) {
						if (currentStaticNode == null) {
							TrussPanel.this.currentStaticNode = TrussPanel.this.nodeList
									.getNode(i);
						} else {
							currentStaticNode.setColor(Color.BLACK);
							currentStaticNode = TrussPanel.this.nodeList
									.getNode(i);
						}
					}
				}
				if (currentStaticNode != null) {
					currentStaticNode.setColor(Color.BLUE);
					double x, y;
					x = (currentStaticNode.getLocation().x - origin
							.getLocation().x) / 10.0;
					y = -(currentStaticNode.getLocation().y - origin
							.getLocation().y) / 10.0;
					instruction.setText("x: ");
					input1.setText("" + x);
					instruction2.setText(" y: ");
					input2.setText("" + y);
					infoPanel.add(input1);
					infoPanel.add(instruction2);
					infoPanel.add(input2);
					repaint();
				}
			} else if (operation == operations.AddFixNode) {
				for (int i = 1; i < TrussPanel.this.nodeList.size(); i++) {
					if (TrussPanel.this.nodeList.getNode(i).contains(
							e.getPoint())) {
						currentStaticNode = nodeList.getNode(i);
						if (fixNode == null) {
							fixNode = new FixNode(currentStaticNode, 0);
							currentStaticNode.addFixNode(fixNode);
						} else {
							fixNode.getLocation().removeFixNode();
							fixNode = new FixNode(currentStaticNode, 0);
							currentStaticNode.addFixNode(fixNode);
						}
						repaint();
					}
				}
			} else if (operation == operations.AddRoller) {
				for (int i = 1; i < TrussPanel.this.nodeList.size(); i++) {
					if (TrussPanel.this.nodeList.getNode(i).contains(
							e.getPoint())) {
						currentStaticNode = nodeList.getNode(i);
						if (roller == null) {
							roller = new Roller(currentStaticNode, 0);
							currentStaticNode.addRoller(roller);
						} else {
							roller.getLocation().removeRoller();
							roller = new Roller(currentStaticNode, 0);
							currentStaticNode.addRoller(roller);
						}
						currentStaticNode.addRoller(new Roller(
								currentStaticNode, 0));
						instruction.setText("Set Degree: ");
						input1.setText(""
								+ ((int) currentStaticNode.getRoller()
										.getDirection() * 10) / 10.0);
						infoPanel.add(input1);
						repaint();

					}
				}
			}
		}
	}

	private class Drag extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			if (TrussPanel.this.allowDrag && operation == operations.Drag) {
				for (int i = 0; i < TrussPanel.this.nodeList.size(); i++) {
					if (TrussPanel.this.nodeList.getNode(i).contains(
							e.getPoint())) {
						if (TrussPanel.this.currentNode == null) {
							TrussPanel.this.currentNode = TrussPanel.this.nodeList
									.getNode(i);
						} else if (!TrussPanel.this.currentNode.contains(e
								.getPoint())) {
							TrussPanel.this.currentNode = TrussPanel.this.nodeList
									.getNode(i);
						}
					}
				}
				if (TrussPanel.this.currentNode != null) {
					TrussPanel.this.currentNode.changeLocation(e.getPoint());
					double x, y;
					x = (currentNode.getLocation().x - origin.getLocation().x) / 10.0;
					y = -(currentNode.getLocation().y - origin.getLocation().y) / 10.0;
					if (currentNode.equals(origin)) {
						instruction.setText("Changing the Location of Origin");
					} else {
						instruction.setText("x: " + x + " y: " + y);
					}
					calculate();
					TrussPanel.this.repaint();
				}

			} else if (TrussPanel.this.operation == TrussPanel.operations.AddLoads) {
				for (int i = 1; i < TrussPanel.this.nodeList.size(); i++) {
					if (TrussPanel.this.nodeList.getNode(i).contains(
							e.getPoint())) {
						if (TrussPanel.this.currentStaticNode == null) {
							TrussPanel.this.currentStaticNode = TrussPanel.this.nodeList
									.getNode(i);
						} else if (!TrussPanel.this.currentStaticNode
								.contains(e.getPoint())) {
							TrussPanel.this.currentStaticNode = TrussPanel.this.nodeList
									.getNode(i);
						}
					}
				}
				if (TrussPanel.this.currentStaticNode != null) {
					TrussPanel.this.currentStaticNode.setLoad(new Load(
							TrussPanel.this.currentStaticNode.getLocation(), e
									.getPoint()));
					instruction.setText("Magnitude: ");
					input1.setText(""
							+ (int) (currentStaticNode.getLoad().getForce() * 10)
							/ 10.0);
					instruction2.setText(" Direction: ");
					input2.setText(""
							+ (int) (Math.toDegrees(currentStaticNode.getLoad()
									.getForceDirection() * 10)) / 10.0);
					infoPanel.add(input1);
					infoPanel.add(instruction2);
					infoPanel.add(input2);
					calculate();
					TrussPanel.this.repaint();
				}
			}
		}
	}

	private class DrawPanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setColor(Color.GRAY);
			int width = drawPanel.getWidth();
			int height = drawPanel.getHeight();
			for (int i = 0; i <= width; i += 50) {
				g.drawLine(i, 0, i, height);
			}
			for (int i = 0; i <= height; i += 50) {
				g.drawLine(0, i, width, i);
			}

			for (int i = 1; i < TrussPanel.this.nodeList.size(); i++) {
				TrussPanel.this.nodeList.getNode(i).draw(g);
			}
			for (int i = 0; i < TrussPanel.this.trussList.size(); i++)
				TrussPanel.this.trussList.getTruss(i).draw(g);
			origin.draw(g);
		}
	}

	private class Release extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			TrussPanel.this.currentNode = null;
		}
	}

	private class AddLoads extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			allowDrag = false;
			drag.setText("Drag Off");
			TrussPanel.this.operation = TrussPanel.operations.AddLoads;
			TrussPanel.this.firstClick = true;
			instruction.setText("Click on a Node and Drag");
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			if (currentStaticNode != null) {
				currentStaticNode.setColor(Color.BLACK);
				repaint();
			}
		}
	}

	private class AddNodes extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			TrussPanel.this.operation = TrussPanel.operations.AddNodes;
			TrussPanel.this.firstClick = true;
			instruction.setText("Click to Add Nodes");
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			if (currentStaticNode != null) {
				currentStaticNode.setColor(Color.BLACK);
				repaint();
			}
		}
	}

	private class AddTrusses extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			TrussPanel.this.operation = TrussPanel.operations.AddTrusses;
			TrussPanel.this.firstClick = true;
			instruction.setText("Click two Nodes to Add a Member");
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			if (currentStaticNode != null) {
				currentStaticNode.setColor(Color.BLACK);
				repaint();
			}
		}
	}

	private class AllowDrag extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			operation = operations.Drag;
			if (!TrussPanel.this.allowDrag) {
				TrussPanel.this.allowDrag = true;
				TrussPanel.this.drag.setText("Drag On");
				instruction.setText("Drag a Node");
			} else {
				TrussPanel.this.allowDrag = false;
				TrussPanel.this.drag.setText("Drag Off");
			}
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			if (currentStaticNode != null) {
				currentStaticNode.setColor(Color.BLACK);
				repaint();
			}
		}
	}

	private class RemoveLoads extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			TrussPanel.this.operation = TrussPanel.operations.RemoveLoads;
			TrussPanel.this.firstClick = true;
			instruction.setText("Click Nodes to Remove Loads");
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			if (currentStaticNode != null) {
				currentStaticNode.setColor(Color.BLACK);
				repaint();
			}
		}
	}

	private class RemoveNodes extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			TrussPanel.this.operation = TrussPanel.operations.RemoveNodes;
			TrussPanel.this.firstClick = true;
			instruction.setText("Click a Node to Remove It");
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			if (currentStaticNode != null) {
				currentStaticNode.setColor(Color.BLACK);
				repaint();
			}
		}
	}

	private class RemoveTrusses extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			TrussPanel.this.operation = TrussPanel.operations.RemoveTrusses;
			TrussPanel.this.firstClick = true;
			instruction.setText("Click Two Nodes to Remove Member");
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			if (currentStaticNode != null) {
				currentStaticNode.setColor(Color.BLACK);
				repaint();
			}
		}
	}

	private class Select extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			operation = operations.Select;
			firstClick = true;
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			instruction.setText("Click a Node to Set Its Coordinates");
		}
	}

	private class Calculate extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			operation = operations.Calculate;
			calculate();
		}
	}

	private class ClearAll extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int n = JOptionPane.showConfirmDialog(drawPanel, "Clear All?",
					"Clear All", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				nodeList = null;
				nodeList = new NodeList();
				nodeList.addNode(origin);
				trussList = null;
				trussList = new TrussList();
				fixNode = null;
				roller = null;
				tempNode1 = null;
				tempNode2 = null;
				currentNode = null;
				currentStaticNode = null;
				infoPanel.remove(instruction2);
				infoPanel.remove(input1);
				infoPanel.remove(input2);
				instruction.setText("");
				repaint();
			}
		}
	}

	private class AddFixNode extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			operation = operations.AddFixNode;
			firstClick = true;
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			instruction.setText("Set a Node as Fix Node");
		}
	}

	private class AddRoller extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			operation = operations.AddRoller;
			firstClick = true;
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
			instruction.setText("Set a Node as Roller");
		}
	}

	private class Input implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String xstring = input1.getText();
			String ystring = input2.getText();
			double x, y;
			if (operation == operations.Select) {
				try {
					x = Double.parseDouble(xstring) * 10;
					y = Double.parseDouble(ystring) * 10;
					int width = drawPanel.getWidth();
					int height = drawPanel.getHeight();
					if ((x + origin.getLocation().x) < width
							&& (x + origin.getLocation().x) >= 0
							&& (origin.getLocation().y - y) < height
							&& (origin.getLocation().y - y) > 0) {
						currentStaticNode.changeLocation(new Point(
								(int) (x + origin.getLocation().x),
								(int) (origin.getLocation().y - y)));

						infoPanel.remove(instruction2);
						infoPanel.remove(input1);
						infoPanel.remove(input2);
						instruction.setText("x: " + ((int) x) / 10.0 + " y: "
								+ ((int) y) / 10.0);
						repaint();
					} else
						throw new Exception(
								"Numbers Out of Bound. Try Again: x:");
				} catch (NumberFormatException ee) {
					instruction.setText("Please Enter Numbers");
				} catch (Exception eee) {
					instruction.setText(eee.getMessage());
				}
			} else if (operation == operations.AddLoads) {
				try {
					x = Double.parseDouble(xstring);
					y = Double.parseDouble(ystring);
					currentStaticNode.getLoad().setForceMagnitude(x);
					currentStaticNode.getLoad().setForceDirection(y);
					infoPanel.remove(instruction2);
					infoPanel.remove(input1);
					infoPanel.remove(input2);
					instruction.setText("Magnitude: " + ((int) (x * 10)) / 10.0
							+ " Direction: " + (((int) (y * 10)) / 10.0));
					repaint();
				} catch (NumberFormatException ee) {
					instruction.setText("Please Enter Numbers");
				}
			} else if (operation == operations.AddFixNode) {
				try {
					x = Double.parseDouble(xstring);
					currentStaticNode.getFixNode().setDirection(x);
					;
					infoPanel.remove(input1);
					instruction.setText("Degree: " + (((int) (x * 10)) / 10.0));
					repaint();
				} catch (NumberFormatException ee) {
					instruction.setText("Please Enter a Number");
				}
			} else if (operation == operations.AddRoller) {
				try {
					x = Double.parseDouble(xstring);
					currentStaticNode.getRoller().setDirection(x);
					;
					infoPanel.remove(input1);
					instruction.setText("Degree: " + (((int) (x * 10)) / 10.0));
					repaint();
				} catch (NumberFormatException ee) {
					instruction.setText("Please Enter a Number");
				}
			}
		}
	}

	private void calculate() {
		firstClick = true;
		if (operation == operations.Calculate) {
			infoPanel.remove(instruction2);
			infoPanel.remove(input1);
			infoPanel.remove(input2);
		}
		if ((2 * (nodeList.size() - 1) == (trussList.size() + 3)
				&& fixNode != null && roller != null)) {
			Matrix mat = new Matrix(2 * (nodeList.size() - 1),
					2 * (nodeList.size()) - 1);
			for (int i = 1; i < nodeList.size(); i++) {
				Node tempNode = nodeList.getNode(i);
				for (int j = 0; j < tempNode.getTrussArray().size(); j++) {
					Truss tempTruss = tempNode.getTrussArray().get(j);
					int count = 0;
					for (int trace = 0; trace < trussList.size(); trace++) {
						if (trussList.getTruss(trace).equals(tempTruss)) {
							count = trace;
						}
					}
					if (tempNode.getLocation().y >= tempNode.getTheOtherNode(
							tempTruss).getLocation().y) {
						mat.setElement(tempTruss.getSin(), 2 * (i - 1), count);
					} else
						mat.setElement(-tempTruss.getSin(), 2 * (i - 1), count);
					if (tempNode.getLocation().x >= tempNode.getTheOtherNode(
							tempTruss).getLocation().x) {
						mat.setElement(-tempTruss.getCos(), 2 * (i - 1) + 1,
								count);
					} else
			 			mat.setElement(tempTruss.getCos(), 2 * (i - 1) + 1,
								count);
				}
				if (tempNode.equals (fixNode.getLocation())) {
					mat.setElement(1, 2 * (i - 1), 2 * nodeList.size() - 5);
					mat.setElement(1, 2 * (i - 1) + 1, 2 * nodeList.size() - 4);
				}
				if (tempNode.equals(roller.getLocation())) {
					mat.setElement(roller.getSin(), 2 * (i - 1), 2 * nodeList.size() - 3);
					mat.setElement(roller.getCos(), 2 * (i - 1)+1, 2 * nodeList.size() - 3);
				}
				if (tempNode.getLoad() != null) {
					mat.setElement(-tempNode.getLoad().getSin()
							* tempNode.getLoad().getForce(), 2 * (i - 1),
							2 * nodeList.size() - 2);
					mat.setElement(-tempNode.getLoad().getCos()
							* tempNode.getLoad().getForce(), 2 * (i - 1) + 1,
							2 * nodeList.size() - 2);
				}
			}
			if (operation == operations.Calculate)
				instruction.setText("Red: Compression  Blue: Tension");
			double[] result = mat.solveAugmented();
			for (int i = 0; i < trussList.size(); i++) {
				trussList.getTruss(i).setValue(result[i]);
				trussList.getTruss(i).setValueDisplay(true);
			}
			fixNode.setValue1(result[result.length - 2]);
			fixNode.setValue2(-result[result.length - 3]);
			roller.setValue1(-roller.getSin()*result[result.length - 1]);
			roller.setValue2(roller.getCos()*result[result.length - 1]);
			repaint();
		} else if (operation == operations.Calculate) {
			instruction
					.setText("(Number of Members + 3) must be equal to (2*number of Nodes)");
		}
	}

	private static enum operations {
		AddNodes, RemoveNodes, AddTrusses, RemoveTrusses, AddLoads, RemoveLoads, Drag, Select, AddFixNode, AddRoller, Calculate
	}
}