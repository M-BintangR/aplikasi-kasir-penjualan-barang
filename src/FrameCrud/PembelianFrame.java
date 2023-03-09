/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameCrud;
import Database.Koneksi;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Muhammad bintang
 */
public class PembelianFrame extends javax.swing.JFrame {
     private DefaultTableModel model = null;
     Koneksi k = new Koneksi();
     private PreparedStatement stat;
     private ResultSet rs;
     private String kodeBarang, member = "tidak";
    /**
     * Creates new form PembelianFrame
     */
    public PembelianFrame() {
        initComponents();
        k.connect();
        this.recordTable();
        this.setEnableComponent();
        this.getTanggal();
        this.relasiBarang();
        this.setDefaultComponent();
        
    }
    
    private void setEnableComponent(){
        this.inputDiskon.setEnabled(false);
        this.inputTotal.setEnabled(false);
        this.inputHargaSatuan.setEnabled(false);
        this.inputTanggalBayar.setEnabled(false);
        this.inputId.setText("");
        this.inputId.setVisible(false);
    }
    
    private void setDefaultComponent(){
        this.inputDiskon.setText("0");
        this.inputTotal.setText("0");
    }
    
    private void clearComponent(){
        this.inputNamaPembeli.setText("");
        this.inputJumlahBarang.setText("");
        this.inputHargaSatuan.setText("");
        this.inputDiskon.setText("");
        this.inputTotal.setText("");
        this.inputTanggalBayar.setText("");
        
    }
    
    //! MENGAMBIL RELASI DARI TBL BARANG
     public void relasiBarang(){
       try{
        stat = k.getCon().prepareStatement("SELECT * FROM tbl_barang");
        rs = stat.executeQuery();
        
        this.rs = this.stat.executeQuery();
        
        while(rs.next()){
            this.inputKodeBarang.addItem(rs.getString("kode_barang") + ":" + rs.getString("nama_barang"));
        }
        this.getKodeBarang();
        this.getHargaSatuan();
       }catch(Exception e){
           JOptionPane.showMessageDialog(null, e.getMessage());
       }
    }
     
    public void getTanggal(){
        LocalDate currentDate = LocalDate.now();
        this.inputTanggalBayar.setText(currentDate.toString());
    }
    
    public void getHargaSatuan(){
        try{
        stat = k.getCon().prepareStatement("SELECT * FROM tbl_barang WHERE kode_barang=?");
        stat.setString(1, this.kodeBarang);
        rs = stat.executeQuery();
        
        this.rs = this.stat.executeQuery();
        
        while(rs.next()){
            
            this.inputHargaSatuan.setText(rs.getString("harga_barang"));
        }
       }catch(Exception e){
           JOptionPane.showMessageDialog(null, e.getMessage());
       }
    }
     
    public void getKodeBarang(){
        String[]  kBarang;
        kBarang = this.inputKodeBarang.getSelectedItem().toString().split(":");
        this.kodeBarang = kBarang[0].toString();
    }
    
    public void getTotal(){ 
        double diskon = 0;
        if(this.inputMember.isSelected()){
          diskon = 0.1;
          this.member = "ya";
       }else{
         diskon = 0;
         this.member = "tidak";
       }
        int JmlBarang = Integer.parseInt(this.inputJumlahBarang.getText());
        int HargaSatuan = Integer.parseInt(this.inputHargaSatuan.getText());
        double HasilDiskon = HargaSatuan * diskon;
        double total = (HargaSatuan * JmlBarang) - HasilDiskon;
//        double diskon = Double.parseDouble(this.inputDiskon.getText());
//        double result = total - diskon;
        this.inputDiskon.setText(String.valueOf(HasilDiskon));
        this.inputTotal.setText(String.valueOf(total));
    }
    
  
    //! RECORD TABEL
    public void recordTable(){
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("NAMA PEMBELI");
        model.addColumn("KODE BARANG");
        model.addColumn("JUMLAH PEMBELIAN");
        model.addColumn("HARGA SATUAN");
        model.addColumn("DISKON");
        model.addColumn("MEMBER");
        model.addColumn("TOTAL PEMBELIAN");
        model.addColumn("TANGGAL PEMBELIAN");
        this.tblPembeli.setModel(model);
        try {
           stat = k.getCon().prepareStatement("SELECT * FROM tbl_pembeli");
           rs = stat.executeQuery();
           while(rs.next()){
               Object[] data = {
                   rs.getString("id"),
                   rs.getString("nama_pembeli"),
                   rs.getString("kode_barang"),
                   rs.getString("jumlah_barang"),
                   rs.getString("harga_satuan"),
                   rs.getString("diskon"),
                   rs.getString("member"),
                   rs.getString("total_harga"),
                   rs.getString("tanggal_beli"),
               };
            model.addRow(data);
        }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        this.clearComponent();
    }
    
    //! PROSES REKAM
    public void onHandleRekam(){
           try {
            stat = k.getCon().prepareStatement("INSERT INTO tbl_pembeli VALUES (?,?,?,?,?,?,?,?,?)");
            stat.setInt(1,0);
            stat.setString(2, this.inputNamaPembeli.getText());
            stat.setString(3, this.kodeBarang);
            stat.setString(4, this.inputJumlahBarang.getText());
            stat.setString(5, this.inputHargaSatuan.getText());
            stat.setString(6, this.inputDiskon.getText());
            stat.setString(7, this.member);
            stat.setString(8, this.inputTotal.getText());
            stat.setString(9, this.inputTanggalBayar.getText());
            stat.executeUpdate();
            this.recordTable();
            JOptionPane.showMessageDialog(null, "PEMBAYARAN SUKSES!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    //! PROSES HAPUS
    public void onHandleHapus(){
        try{
            stat = k.getCon().prepareStatement("DELETE FROM tbl_pembeli WHERE id= ?");
            stat.setString(1, this.inputId.getText());
            stat.executeUpdate();
            this.recordTable();
            JOptionPane.showMessageDialog(null, "DATA PEMBAYARAN BERHASIL DI HAPUS !");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    //! CLICK TABEL
    public void onHandleClick(){
          this.inputId.setText(model.getValueAt(tblPembeli.getSelectedRow(), 0).toString());
          this.inputNamaPembeli.setText(model.getValueAt(tblPembeli.getSelectedRow(), 1).toString());
          this.inputKodeBarang.setSelectedItem(model.getValueAt(tblPembeli.getSelectedRow(), 2).toString());
          this.inputJumlahBarang.setText(model.getValueAt(tblPembeli.getSelectedRow(), 3).toString());
          this.inputHargaSatuan.setText(model.getValueAt(tblPembeli.getSelectedRow(), 4).toString());
          this.inputDiskon.setText(model.getValueAt(tblPembeli.getSelectedRow(), 5).toString());
          this.member = (model.getValueAt(tblPembeli.getSelectedRow(), 6).toString());
          this.inputTotal.setText(model.getValueAt(tblPembeli.getSelectedRow(), 7).toString());
          this.inputTanggalBayar.setText(model.getValueAt(tblPembeli.getSelectedRow(), 8).toString());
    }
    
    //! PROSES EDIT
    public void onHandleEdit(){
         try {       
             stat = k.getCon().prepareStatement("UPDATE tbl_pembeli SET nama_pembeli=?,"
            + "kode_barang=?, jumlah_barang=?, harga_satuan=?, diskon=?, member=?, total_harga=?, tanggal_beli=? WHERE id=?");
            stat.setString(1, this.inputNamaPembeli.getText());
            stat.setString(2, this.kodeBarang);
            stat.setString(3, this.inputJumlahBarang.getText());
            stat.setString(4, this.inputHargaSatuan.getText());
            stat.setString(5, this.inputDiskon.getText());
            stat.setString(6, this.member);
            stat.setString(7, this.inputTotal.getText());
            stat.setString(8, this.inputTanggalBayar.getText());
            stat.setString(9, this.inputId.getText());
            stat.executeUpdate();
            this.recordTable();
            JOptionPane.showMessageDialog(null, "DATA PEMBAYARAN BERHASIL DI UBAH !");
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        inputNamaPembeli = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnLihatBarang = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        inputKodeBarang = new javax.swing.JComboBox<>();
        inputHargaSatuan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        inputDiskon = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        inputTanggalBayar = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        inputTotal = new javax.swing.JTextField();
        btnHitung = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPembeli = new javax.swing.JTable();
        btnRekam = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnHapus = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        inputJumlahBarang = new javax.swing.JTextField();
        btnKosongkan = new javax.swing.JButton();
        inputId = new javax.swing.JTextField();
        inputMember = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("KASIR BARANG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addContainerGap(1192, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel2.setText("MASUKKAN DATA PEMBELI");

        jLabel3.setText("Nama Pembeli");

        jLabel4.setText("Masukkan Kode Barang Pembeliani");

        btnLihatBarang.setText("LIHAT BARANG");

        jLabel5.setText("Masukkan Jumlah Barang");

        jLabel6.setText("Harga Satuan");

        inputKodeBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputKodeBarangActionPerformed(evt);
            }
        });

        jLabel7.setText("Diskon");

        jLabel8.setText("Member Untuk  Diskon  10%");

        jLabel9.setText("Tanggal Pembayaran");

        jLabel10.setText("TOTAL KESELURUHAN");

        btnHitung.setText("TOTAL KESELURUHAN");
        btnHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungActionPerformed(evt);
            }
        });

        tblPembeli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPembeli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPembeliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPembeli);

        btnRekam.setText("REKAM DATA PEMBELIAN");
        btnRekam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekamActionPerformed(evt);
            }
        });

        jLabel11.setText("TABEL DATA PEMBELIAN");

        jLabel12.setText("KLIK BARIS DATA JIKA INGIN HAPUS / EDIT DATA");

        btnHapus.setText("HAPUS DATA");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnEdit.setText("EDIT DATA");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jLabel13.setText("ACTION");

        btnKosongkan.setText("KOSONG KAN DATA");
        btnKosongkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKosongkanActionPerformed(evt);
            }
        });

        inputMember.setText("Klik Untuk Member");
        inputMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputMemberActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(inputNamaPembeli, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(inputKodeBarang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(btnLihatBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputMember)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(inputJumlahBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(inputTanggalBayar, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnRekam, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                            .addComponent(inputDiskon, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputHargaSatuan, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputTotal)
                            .addComponent(btnHitung, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                            .addComponent(btnKosongkan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12)
                            .addComponent(jLabel11)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 861, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8)
                    .addComponent(jLabel13))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inputNamaPembeli, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(inputMember)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnLihatBarang, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                            .addComponent(inputKodeBarang))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputTanggalBayar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                            .addComponent(inputJumlahBarang))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                            .addComponent(inputHargaSatuan))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnHitung, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                            .addComponent(inputDiskon)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnKosongkan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRekam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(inputId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungActionPerformed

        this.getTotal();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHitungActionPerformed

    private void btnRekamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekamActionPerformed
        this.onHandleRekam();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRekamActionPerformed

    private void btnKosongkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKosongkanActionPerformed
        this.clearComponent();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKosongkanActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        this.onHandleEdit();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        this.onHandleHapus();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusActionPerformed

    private void tblPembeliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPembeliMouseClicked
        this.onHandleClick();
        // TODO add your handling code here:
    }//GEN-LAST:event_tblPembeliMouseClicked

    private void inputMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputMemberActionPerformed
       
        // TODO add your handling code here:
    }//GEN-LAST:event_inputMemberActionPerformed

    private void inputKodeBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputKodeBarangActionPerformed
        this.relasiBarang();
        // TODO add your handling code here:
    }//GEN-LAST:event_inputKodeBarangActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PembelianFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PembelianFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PembelianFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PembelianFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PembelianFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHitung;
    private javax.swing.JButton btnKosongkan;
    private javax.swing.JButton btnLihatBarang;
    private javax.swing.JButton btnRekam;
    private javax.swing.JTextField inputDiskon;
    private javax.swing.JTextField inputHargaSatuan;
    private javax.swing.JTextField inputId;
    private javax.swing.JTextField inputJumlahBarang;
    private javax.swing.JComboBox<String> inputKodeBarang;
    private javax.swing.JCheckBox inputMember;
    private javax.swing.JTextField inputNamaPembeli;
    private javax.swing.JTextField inputTanggalBayar;
    private javax.swing.JTextField inputTotal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPembeli;
    // End of variables declaration//GEN-END:variables
}
