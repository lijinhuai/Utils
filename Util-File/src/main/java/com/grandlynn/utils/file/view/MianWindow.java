package com.grandlynn.utils.file.view;

import com.grandlynn.utils.file.config.Constant;
import com.grandlynn.utils.file.control.FileEncAndDec;
import com.grandlynn.utils.file.control.FileOperate;
import com.grandlynn.utils.file.control.ZipUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

public class MianWindow extends JFrame {

    public static String[] FILEPATH = {"文稿", "下载", "桌面", "文稿", "下载"};
    public static String localPath = "";
    public static String RButtonSelect = "";
    private FileOperate operate = new FileOperate();


    public JPanel panel = new JPanel();
    public JPanel panel1 = new JPanel();
    public JPanel panelLeft = new JPanel();
    public JScrollPane scoll;
    public JPanel panelRight = new JPanel();
    public JButton btnBack;
    public MouseRightPopup popup;
    public JButton btnNext;
    public JButton btnUndo;
    public JButton btnNew;
    public JButton btnNewfile;
    public JButton kong;
    public JList list;
    private DefaultListModel<String> item;
    public JLabel label1 = new JLabel("     ");
    public JLabel label2 = new JLabel("     ");

    public MianWindow(String str) {
        setTitle("文件资源管理器");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screen = kit.getScreenSize();
        this.setSize(screen.width / 2, screen.height / 2);
        this.setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init(str);
        mouseControlFilelist();
        mouseRightMenuFunction();
        mouseControlFile();
        BtnListenner();
    }

    public void layoutTop() {
        btnBack = new JButton(new ImageIcon("./image/back.png"));
        btnNext = new JButton(new ImageIcon("./image/next.png"));
        btnUndo = new JButton("返回");
        btnNew = new JButton("新建文件夹");
        btnNewfile = new JButton("新建文件");
        kong = new JButton(" ");
        item = new DefaultListModel<>();
        list = new JList<String>(item);
        //list = new JList(Constant.STR);
        initList();

        popup = new MouseRightPopup();
    }

    public void initList() {
        item.removeAllElements();
        for (int i = 0; i < Constant.LIST.size(); i++) {
            item.addElement(Constant.LIST.get(i));
        }


    }

    public void init(String str) {
        layoutTop();
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        this.add(btnBack);
        this.add(btnNext);
        this.add(label1);
        this.add(label2);
        this.add(kong);
        this.add(btnUndo);
        this.add(btnNew);
        this.add(btnNewfile);
        this.add(panel1);
        this.add(list);

        panelRightLayout(str);
        panelRight.setPreferredSize(new Dimension(0, 1500));
        scoll = new JScrollPane(panelRight);
        this.add(scoll);
        panelRight.setBackground(Color.WHITE);
        kong.setBackground(null);
        list.setSelectionBackground(Color.LIGHT_GRAY);
        list.setBackground(Color.getColor("#fe1"));
        list.setFixedCellHeight(28);
        list.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panelRight.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        GridBagConstraints constraints = new GridBagConstraints();//定义一个GridBagConstraints，用来控制组建的显示位置
        constraints.fill = GridBagConstraints.BOTH;
        //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
        //NONE：不调整组件大小。
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
        //BOTH：使组件完全填满其显示区域。

        constraints.gridwidth = 1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        constraints.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        constraints.weighty = 0;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        layout.setConstraints(btnBack, constraints);//设置组件
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        layout.setConstraints(btnNext, constraints);
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        layout.setConstraints(label1, constraints);
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        layout.setConstraints(label2, constraints);
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        layout.setConstraints(kong, constraints);
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        layout.setConstraints(btnUndo, constraints);
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        layout.setConstraints(btnNew, constraints);
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        layout.setConstraints(btnNewfile, constraints);
        constraints.gridwidth = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        layout.setConstraints(panel1, constraints);
        constraints.gridwidth = 4;
        constraints.weightx = 0;
        constraints.weighty = 1;
        layout.setConstraints(list, constraints);
        constraints.gridwidth = 5;
        constraints.weightx = 1;
        constraints.weighty = 1;
        layout.setConstraints(scoll, constraints);
    }


    public void panelRightLayout(String str) {
        openFile(str);
        panelRight.setLayout(new FlowLayout(0, 10, 10));
    }


    public boolean openFile(String path) {
        panelRight.removeAll();
        removeAll();
        showpath(path);
        File f = new File(path);
        if (f != null && f.isDirectory()) {
            FILEPATH = f.list();
            if (FILEPATH == null) {
                FILEPATH = new String[1];
                FILEPATH[0] = "";
            } else {
                for (int i = 0; i < FILEPATH.length; i++) {
                    final JPanel panel_item = new JPanel();
                    ImageIcon icon = new ImageIcon("./image/wenjianjia.png");
                    ImageIcon icon_file = new ImageIcon("./image/wenjian.png");
                    JLabel label;
                    File file = new File(path + "/" + FILEPATH[i]);
                    if (file.isDirectory()) {
                        label = new JLabel(icon);
                    } else {
                        label = new JLabel(icon_file);
                    }
                    JLabel label1 = new JLabel(FILEPATH[i]);
                    label1.setPreferredSize(new Dimension(50, 20));
                    panel_item.setLayout(new BorderLayout());
                    panel_item.setBackground(null);
                    panel_item.add(label, BorderLayout.NORTH);
                    panel_item.add(label1, BorderLayout.CENTER);
                    label1.setHorizontalAlignment(SwingConstants.CENTER);
                    panel_item.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            //设置左键双击打开文件夹
                            if (e.getClickCount() == 1) {
                                if (panel_item.getBackground() == Color.lightGray) {
                                    panel_item.setBackground(null);
                                } else {
                                    panel_item.setBackground(Color.lightGray);
                                }
                            }

                            if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                                JPanel item = (JPanel) e.getComponent();
                                JLabel la = (JLabel) item.getComponent(1);
                                String str = la.getText();
                                openFile(localPath + File.separator + str);
                            }


                            //设置右键显示菜单
                            if (e.getButton() == MouseEvent.BUTTON3) {
                                panel_item.setBackground(Color.lightGray);
                                JPanel item = (JPanel) e.getComponent();
                                JLabel la = (JLabel) item.getComponent(1);
                                RButtonSelect = la.getText();
                                popup.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    });
                    panelRight.add(panel_item);
                }
            }
            localPath = path;
            return true;
        } else {
            return false;
        }

    }

    public void showpath(String path)  //显示路径字符串
    {
        kong.setText(path);
    }

    @Override
    public void removeAll() {
        panelRight.repaint();
    }

    //右键
    public void mouseRightMenuFunction() {
        ActionListener itemAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO 自动生成的方法存根
                String temp = e.getActionCommand();
                if ("打开".equals(temp)) {
                    System.out.println("dshdfjlghsslh");
                    if (RButtonSelect != "") {
                        openFile(localPath + File.separator + RButtonSelect);
                        RButtonSelect = "";
                        openFile(localPath);
                        panelRight.updateUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "没有文件或文件夹被选中", "提示", JOptionPane.WARNING_MESSAGE);
                    }
                }
                if ("删除".equals(temp)) {
                    try {
                        if (RButtonSelect != "") {
                            operate.deleteFile(localPath + File.separator + RButtonSelect);
                            RButtonSelect = "";
                            openFile(localPath);
                            panelRight.updateUI();
                        } else {
                            JOptionPane.showMessageDialog(null, "没有文件或文件夹被选中", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (FileNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                if ("复制".equals(temp)) {
                    Constant.COPYPARTH = localPath + File.separator + RButtonSelect;
                }
                if ("粘贴".equals(temp)) {
                    String fileName = operate.getFilenameFromPath(Constant.COPYPARTH);

                    operate.copy(Constant.COPYPARTH, localPath + File.separator + operate.getFilenameFromPath(Constant.COPYPARTH));
                    openFile(localPath);
                    panelRight.updateUI();

                }
                if ("压缩".equals(temp)) {
                    ZipUtil.zip(localPath + File.separator + RButtonSelect);
                    openFile(localPath);
                    panelRight.updateUI();
                }
                if ("解压".equals(temp)) {
                    ZipUtil.unzip(localPath + File.separator + RButtonSelect);
                    openFile(localPath);
                    panelRight.updateUI();
                }
                if ("加密".equals(temp)) {
                    try {
                        FileEncAndDec.EncFile(localPath + File.separator + RButtonSelect, localPath + File.separator + "加密" + RButtonSelect);
                        openFile(localPath);
                        panelRight.updateUI();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                if ("解密".equals(temp)) {
                    try {
                        FileEncAndDec.DecFile(localPath + File.separator + RButtonSelect, localPath + File.separator + "解密" + RButtonSelect);
                        openFile(localPath);
                        panelRight.updateUI();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                if ("刷新".equals(temp)) {
                    openFile(localPath);
                    panelRight.updateUI();
                }
                if ("添加到左侧边栏".equals(temp)) {
                    Constant.LIST.add(RButtonSelect);
                    Constant.LISTPATH.add(localPath + File.separator + RButtonSelect);
                    initList();
                    openFile(localPath);
                    panelRight.updateUI();
                }
            }
        };


        popup.addItemListener(0, itemAction);
        popup.addItemListener(1, itemAction);
        popup.addItemListener(2, itemAction);
        popup.addItemListener(3, itemAction);
        popup.addItemListener(4, itemAction);
        popup.addItemListener(5, itemAction);
        popup.addItemListener(6, itemAction);
        popup.addItemListener(7, itemAction);
        popup.addItemListener(8, itemAction);
        popup.addItemListener(9, itemAction);
    }

    private void mouseControlFile() {

        panelRight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //设置右键显示菜单
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    //左侧边栏点击事件
    private void mouseControlFilelist() {

        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // TODO 自动生成的方法存根
                int i = e.getFirstIndex();
                openFile(Constant.LISTPATH.get(i));
            }
        });

    }

    //返回按钮监听器
    public void BtnListenner() {
        btnUndo.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                openFile(operate.pathBackTo(localPath));
            }

        });
        btnBack.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                openFile(operate.pathBackTo(localPath));
            }

        });
        btnNew.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                operate.addDir(localPath + File.separator + "新建文件夹");
                openFile(localPath);
                panelRight.updateUI();
            }

        });
        btnNewfile.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                operate.addFile(localPath + File.separator + "新建文件.txt");
                openFile(localPath);
                panelRight.updateUI();
            }

        });

    }
}
