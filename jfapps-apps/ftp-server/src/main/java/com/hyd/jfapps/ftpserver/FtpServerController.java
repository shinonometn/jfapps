package com.hyd.jfapps.ftpserver;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.FtpServer;

/**
 * @ClassName: FtpServerController
 * @Description: Ftp服务器
 * @author: xufeng
 * @date: 2019/4/25 0025 23:22
 */

@Getter
@Setter
@Slf4j
public class FtpServerController extends FtpServerView {

    private FtpServerService ftpServerService = new FtpServerService(this);

    private ObservableList<FtpServerTableBean> tableData = FXCollections.observableArrayList();

    public void ensureServiceClosed() {
        if (ftpServerService == null) {
            return;
        }

        FtpServer server = ftpServerService.getServer();
        if (server != null && !server.isStopped()) {
            server.stop();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
        initService();
    }

    private void initView() {
        ftpServerService.loadingConfigure();
        userNameTableColumn.setCellValueFactory(new PropertyValueFactory<FtpServerTableBean, String>("userName"));
        userNameTableColumn.setCellFactory(TextFieldTableCell.<FtpServerTableBean>forTableColumn());
        userNameTableColumn.setOnEditCommit((CellEditEvent<FtpServerTableBean, String> t) -> {
            t.getRowValue().setUserName(t.getNewValue());
        });

        passwordTableColumn.setCellValueFactory(new PropertyValueFactory<FtpServerTableBean, String>("password"));
        passwordTableColumn.setCellFactory(TextFieldTableCell.<FtpServerTableBean>forTableColumn());
        passwordTableColumn.setOnEditCommit((CellEditEvent<FtpServerTableBean, String> t) -> {
            t.getRowValue().setPassword(t.getNewValue());
        });

        homeDirectoryTableColumn
            .setCellValueFactory(new PropertyValueFactory<FtpServerTableBean, String>("homeDirectory"));
        homeDirectoryTableColumn.setCellFactory(TextFieldTableCell.<FtpServerTableBean>forTableColumn());
        homeDirectoryTableColumn.setOnEditCommit((CellEditEvent<FtpServerTableBean, String> t) -> {
            t.getRowValue().setHomeDirectory(t.getNewValue());
        });

        downFIleTableColumn.setCellValueFactory(new PropertyValueFactory<FtpServerTableBean, Boolean>("downFIle"));
        downFIleTableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(downFIleTableColumn));
        upFileTableColumn.setCellValueFactory(new PropertyValueFactory<FtpServerTableBean, Boolean>("upFile"));
        upFileTableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(upFileTableColumn));
        deleteFileTableColumn.setCellValueFactory(new PropertyValueFactory<FtpServerTableBean, Boolean>("deleteFile"));
        deleteFileTableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(deleteFileTableColumn));
        isEnabledTableColumn.setCellValueFactory(new PropertyValueFactory<FtpServerTableBean, Boolean>("isEnabled"));
        isEnabledTableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(isEnabledTableColumn));
        tableViewMain.setItems(tableData);
    }

    private void initEvent() {
        tableData.addListener((Change<? extends FtpServerTableBean> tableBean) -> {
            try {
                saveConfigure(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        tableViewMain.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                MenuItem menu_Copy = new MenuItem("复制选中行");
                menu_Copy.setOnAction(event1 -> {
                    FtpServerTableBean tableBean = tableViewMain.getSelectionModel().getSelectedItem();
                    FtpServerTableBean tableBean2 = new FtpServerTableBean(tableBean.getPropertys());
                    tableData.add(tableViewMain.getSelectionModel().getSelectedIndex(), tableBean2);
                });
                MenuItem menu_Remove = new MenuItem("删除选中行");
                menu_Remove.setOnAction(event1 -> {
                    deleteSelectRowAction(null);
                });
                MenuItem menu_RemoveAll = new MenuItem("删除所有");
                menu_RemoveAll.setOnAction(event1 -> {
                    tableData.clear();
                });
                tableViewMain.setContextMenu(new ContextMenu(menu_Copy, menu_Remove, menu_RemoveAll));
            }
        });
        anonymousLoginEnabledCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    anonymousLoginEnabledTextField.setDisable(false);
                    anonymousLoginEnabledButton.setDisable(false);
                } else {
                    anonymousLoginEnabledTextField.setDisable(true);
                    anonymousLoginEnabledButton.setDisable(true);
                }
            }
        });
    }

    private void initService() {
    }

    private void chooseDirectory(TextField toUpdate) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("请选择目录");
        directoryChooser.setInitialDirectory(new File("."));

        File directory = directoryChooser.showDialog(null);
        if (directory != null) {
            toUpdate.setText(directory.getAbsolutePath());
        }
    }

    @FXML
    private void chooseHomeDirectoryAction(ActionEvent event) {
        chooseDirectory(homeDirectoryTextField);
    }

    @FXML
    private void anonymousLoginEnabledAction(ActionEvent event) {
        chooseDirectory(anonymousLoginEnabledTextField);
    }

    @FXML
    private void addItemAction(ActionEvent event) {
        tableData.add(new FtpServerTableBean(true, userNameTextField.getText(), passwordTextField.getText(),
            homeDirectoryTextField.getText(), downFileCheckBox.isSelected(), upFileCheckBox.isSelected(),
            deleteFileCheckBox.isSelected()));
    }

    @FXML
    private void saveConfigure(ActionEvent event) throws Exception {
        ftpServerService.saveConfigure();
    }

    @FXML
    private void otherSaveConfigureAction(ActionEvent event) throws Exception {
        ftpServerService.otherSaveConfigureAction();
    }

    @FXML
    private void loadingConfigureAction(ActionEvent event) {
        ftpServerService.loadingConfigureAction();
    }

    @FXML
    private void deleteSelectRowAction(ActionEvent event) {
        tableData.remove(tableViewMain.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void startAction(ActionEvent event) {
        if ("启动".equals(startButton.getText())) {
            try {
                boolean isTrue = ftpServerService.runFtpServerAction();
                if (isTrue) {
                    startButton.setText("停止");
                }
            } catch (Exception e) {
                log.error("启动失败", e);
            }
        } else {
            boolean isTrue = ftpServerService.stopFtpServerAction();
            if (isTrue) {
                startButton.setText("启动");
            }
        }
    }

    /**
     * 父控件被移除前调用
     */
    public void onCloseRequest(Event event) throws Exception {
        ftpServerService.stopFtpServerAction();
    }
}