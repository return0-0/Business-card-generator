import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.text.MessageFormat;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import java.awt.print.PrinterException;
// 导入所需的库和类

public class NameCardManagerApp {
    // 声明 GUI 组件和数据结构
    private JFrame frame;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nameField, phoneField, emailField, searchField;
    private JButton addButton, updateButton, deleteButton, saveButton, searchButton, sortButton;
    private JButton printButton, generateQRButton, exportButton, importButton;

    private Vector<Vector<String>> data;

    public NameCardManagerApp() {
        // 初始化 GUI 组件并设置布局
        frame = new JFrame("名片通信录");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 500);
        frame.setLayout(new BorderLayout());

        data = loadDataFromFile();
        // 从文件加载数据

        String[] columnNames = {"姓名", "电话", "邮箱"};
        tableModel = new DefaultTableModel(columnNames,0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        for (Vector<String> row : data) {
            tableModel.addRow(row);
            // 将加载的数据添加到表格模型中
        }

        nameField = new JTextField(10);
        phoneField = new JTextField(10);
        emailField = new JTextField(10);
        addButton = new JButton("添加");
        updateButton = new JButton("修改");
        deleteButton = new JButton("删除");
        saveButton = new JButton("保存");
        // 为按钮定义动作监听器

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNameCard();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateNameCard();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteNameCard();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveDataToFile();
            }
        });

        inputPanel.add(new JLabel("姓名:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("电话:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("邮箱:"));
        inputPanel.add(emailField);
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(saveButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        searchField = new JTextField(10);
        searchButton = new JButton("查询");

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchNameCard();
            }
        });

        searchPanel.add(new JLabel("查询条件:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        frame.add(searchPanel, BorderLayout.NORTH);

        sortButton = new JButton("排序");
        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortData();
            }
        });

        inputPanel.add(sortButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        printButton = new JButton("打印");
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printNameCard();
            }
        });
        inputPanel.add(printButton);

        generateQRButton = new JButton("生成二维码");
        generateQRButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateQRCode();
            }
        });
        inputPanel.add(generateQRButton);

        exportButton = new JButton("导出");
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportDataToFile();
            }
        });
        inputPanel.add(exportButton);

        importButton = new JButton("导入");
        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importDataFromFile();
            }
        });
        inputPanel.add(importButton);
        // 将组件添加到框架并设置为可见

        frame.setVisible(true);
    }

    // 根据名称对数据进行排序并刷新表格
    private void sortData() {
        // 对数据进行排序并刷新表格
        data.sort(Comparator.comparing(o -> o.get(0).toLowerCase())); // 根据姓名首字母顺序排序
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Vector<String> rowData : data) {
            tableModel.addRow(new Vector<>(rowData));
        }
    }
    // 使用搜索结果更新表格
    private void searchNameCard() {
        // 根据输入搜索名片，并在新窗口中显示结果
        String searchTerm = searchField.getText();
        DefaultTableModel searchResultModel = new DefaultTableModel(new String[]{"姓名", "电话", "邮箱"}, 0);

        for (Vector<String> rowData : data) {
            if (rowData.get(0).toLowerCase().contains(searchTerm.toLowerCase())) {
                searchResultModel.addRow(new Vector<>(rowData));
            }
        }

        JTable searchResultTable = new JTable(searchResultModel);
        JScrollPane searchResultScrollPane = new JScrollPane(searchResultTable);
        JFrame searchResultFrame = new JFrame("查询结果");
        searchResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchResultFrame.setSize(400, 300);
        searchResultFrame.add(searchResultScrollPane);
        searchResultFrame.setVisible(true);
    }
    // 打印名片
    private void printNameCard() {
        // 实现名片打印的逻辑
        // 可以使用Java打印API或第三方打印库实现名片打印功能
        try {
            MessageFormat header = new MessageFormat("Page {0}");
            MessageFormat footer = new MessageFormat("- {0} -");
            boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            if (complete) {
                JOptionPane.showMessageDialog(frame, "打印完成");
            } else {
                JOptionPane.showMessageDialog(frame, "打印取消");
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(frame, "打印错误，请检查打印设置");
        }
    }
    // 为选定的名片生成 QR 码
    private void generateQRCode() {
        // 为选定的名片生成 QR 码并保存为图像
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            String phone = (String) tableModel.getValueAt(selectedRow, 1);
            String email = (String) tableModel.getValueAt(selectedRow, 2);
            String info = "name: " + name + "\nphone: " + phone + "\nemail: " + email;

            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix;
            try {
                bitMatrix = writer.encode(info, BarcodeFormat.QR_CODE, 300, 300, hintMap);
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", new FileOutputStream("qrcode.png"));
                JOptionPane.showMessageDialog(frame, "二维码已生成并保存为qrcode.png");
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "生成二维码失败: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "请先选择一个人的信息");
        }
    }

    // 将名片数据导出到文件
    private void exportDataToFile() {
        // 实现将名片信息导出到文件的逻辑
        // 可以将名片信息以CSV或其他格式保存到文件中
        try (PrintWriter writer = new PrintWriter(new File("namecards.csv"))) {
            for (Vector<String> rowData : data) {
                String line = String.join(",", rowData);
                writer.println(line);
            }
            JOptionPane.showMessageDialog(frame, "名片信息导出成功");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "名片信息导出失败: " + e.getMessage());
        }
    }
    // 从文件中导入名片数据
    private void importDataFromFile() {
        // 实现从文件导入名片信息的逻辑
        // 可以从文件中读取名片信息，并添加到当前的名片列表中
        try (Scanner scanner = new Scanner(new File("namecards.csv"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                Vector<String> rowData = new Vector<>(Arrays.asList(parts));
                data.add(rowData);
                tableModel.addRow(rowData);
            }
            JOptionPane.showMessageDialog(frame, "名片信息导入成功");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "名片信息导入失败: " + e.getMessage());
        }
    }


    // loadDataFromFile() 和 saveDataToFile() 方法保持不变


    // 从文件加载数据
    private Vector<Vector<String>> loadDataFromFile() {
        // 从文件加载数据并将其作为向量返回
        Vector<Vector<String>> loadedData = new Vector<>();

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:/1/namecards.dat"));
            loadedData = (Vector<Vector<String>>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            // 文件不存在或读取错误，忽略
        }

        return loadedData;
    }
    // 将数据保存到文件
    private void saveDataToFile() {
        // 将当前数据保存到文件
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:/1/namecards.dat"));
            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 保存失败
        }
    }
    // 向表格中添加新名片
    private void addNameCard() {
        // 获取输入数值并向表格中添加新名片
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        Vector<String> rowData = new Vector<>();
        rowData.add(name);
        rowData.add(phone);
        rowData.add(email);

        data.add(rowData);
        tableModel.addRow(rowData);

        clearInputFields();
    }
    // 更新表格中的现有名片
    private void updateNameCard() {
        // 使用新数值更新表格中的选定名片
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            data.get(selectedRow).set(0, name);
            data.get(selectedRow).set(1, phone);
            data.get(selectedRow).set(2, email);

            tableModel.setValueAt(name, selectedRow, 0);
            tableModel.setValueAt(phone, selectedRow, 1);
            tableModel.setValueAt(email, selectedRow, 2);

            clearInputFields();
        }
    }
    // 从表格中删除名片
    private void deleteNameCard() {
        // 从表格中删除选定的名片
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            data.remove(selectedRow);
            tableModel.removeRow(selectedRow);

            clearInputFields();
        }
    }
    // 清除输入字段
    private void clearInputFields() {
        // 清除姓名、电话和电子邮件的输入字段
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    // 启动应用程序的主要方法
    public static void main(String[] args) {
        // 创建 NameCardManagerApp 的实例并从文件加载数据
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NameCardManagerApp app = new NameCardManagerApp();
                app.loadDataFromFile(); // 在应用程序启动时加载数据
            }
        });
    }

}

