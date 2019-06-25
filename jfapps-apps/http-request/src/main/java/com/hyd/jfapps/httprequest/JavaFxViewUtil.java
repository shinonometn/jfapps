package com.hyd.jfapps.httprequest;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class JavaFxViewUtil {

    /**
     * 设置Spinner最大最小值
     */
    public static void setSpinnerValueFactory(Spinner<Integer> spinner, int min, int max) {
        setSpinnerValueFactory(spinner, min, max, min, 1);
    }

    public static void setSpinnerValueFactory(Spinner<Integer> spinner, int min, int max, int initialValue) {
        setSpinnerValueFactory(spinner, min, max, initialValue, 1);
    }

    public static void setSpinnerValueFactory(Spinner<Double> spinner, double min, double max) {
        setSpinnerValueFactory(spinner, min, max, min, 1d);
    }

    public static void setSpinnerValueFactory(Spinner<Double> spinner, double min, double max, double initialValue) {
        setSpinnerValueFactory(spinner, min, max, initialValue, 1d);
    }

    public static void setSpinnerValueFactory(Spinner spinner, Number min, Number max, Number initialValue, Number amountToStepBy) {
        if (min instanceof Integer) {
            IntegerSpinnerValueFactory secondStart_0svf = new IntegerSpinnerValueFactory((int) min, (int) max, (int) initialValue, (int) amountToStepBy);
            spinner.setValueFactory(secondStart_0svf);
            spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    spinner.getValueFactory().setValue(Integer.parseInt(newValue));
                } catch (Exception e) {
                    log.warn("数字int转换异常 newValue:" + newValue);
                    spinner.getEditor().setText(oldValue);
                }
            });
        } else if (min instanceof Double) {
            DoubleSpinnerValueFactory secondStart_0svf = new DoubleSpinnerValueFactory((double) min, (double) max, (double) initialValue, (double) amountToStepBy);
            spinner.setValueFactory(secondStart_0svf);
            spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    spinner.getValueFactory().setValue(Double.parseDouble(newValue));
                } catch (Exception e) {
                    log.warn("数字double转换异常 newValue:" + newValue);
                    spinner.getEditor().setText(oldValue);
                }
            });
        }
    }

    /**
     * @Title: setSliderLabelFormatter
     * @Description: 格式化Slider显示内容
     */
    public static void setSliderLabelFormatter(Slider slider, String formatter) {
        slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                DecimalFormat decimalFormat = new DecimalFormat(formatter);
                return decimalFormat.format(object);
            }

            @Override
            public Double fromString(String string) {
                return Double.valueOf(string);
            }
        });
    }

    /**
     * @Title: addTableViewOnMouseRightClickMenu
     * @Description: 添加TableView右键菜单
     */
    public static void addTableViewOnMouseRightClickMenu(TableView<Map<String, String>> tableView) {
        tableView.setEditable(true);
        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                MenuItem menuAdd = new MenuItem("添加行");
                menuAdd.setOnAction(event1 -> {
                    tableView.getItems().add(new HashMap<String, String>());
                });
                MenuItem menu_Copy = new MenuItem("复制选中行");
                menu_Copy.setOnAction(event1 -> {
                    Map<String, String> map = tableView.getSelectionModel().getSelectedItem();
                    Map<String, String> map2 = new HashMap<String, String>(map);
                    tableView.getItems().add(tableView.getSelectionModel().getSelectedIndex(), map2);
                });
                MenuItem menu_Remove = new MenuItem("删除选中行");
                menu_Remove.setOnAction(event1 -> {
                    tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
                });
                MenuItem menu_RemoveAll = new MenuItem("删除所有");
                menu_RemoveAll.setOnAction(event1 -> {
                    tableView.getItems().clear();
                });
                tableView.setContextMenu(new ContextMenu(menuAdd, menu_Copy, menu_Remove, menu_RemoveAll));
            }
        });
    }

    /**
     * @Title: addListViewOnMouseRightClickMenu
     * @Description: 添加ListView右键菜单
     */
    public static void addListViewOnMouseRightClickMenu(ListView<String> listView) {
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView());
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                MenuItem menuAdd = new MenuItem("添加行");
                menuAdd.setOnAction(event1 -> {
                    listView.getItems().add("");
                });
                MenuItem menu_Copy = new MenuItem("复制选中行");
                menu_Copy.setOnAction(event1 -> {
                    listView.getItems().add(listView.getSelectionModel().getSelectedIndex(), listView.getSelectionModel().getSelectedItem());
                });
                MenuItem menu_Remove = new MenuItem("删除选中行");
                menu_Remove.setOnAction(event1 -> {
                    listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
                });
                MenuItem menu_RemoveAll = new MenuItem("删除所有");
                menu_RemoveAll.setOnAction(event1 -> {
                    listView.getItems().clear();
                });
                listView.setContextMenu(new ContextMenu(menuAdd, menu_Copy, menu_Remove, menu_RemoveAll));
            }
        });
    }

    /**
     * @Title: setSpinnerValueFactory
     * @Description: 初始化表格属性
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setTableColumnMapValueFactory(TableColumn tableColumn, String name) {
        setTableColumnMapValueFactory(tableColumn, name, true);
        // tableColumn.setOnEditCommit((CellEditEvent<Map<String, String>,
        // String> t)-> {
        // t.getRowValue().put(name, t.getNewValue());
        // });
    }

    public static void setTableColumnMapValueFactory(TableColumn tableColumn, String name, boolean isEdit) {
        tableColumn.setCellValueFactory(new MapValueFactory(name));
        tableColumn.setCellFactory(TextFieldTableCell.<Map<String, String>>forTableColumn());
        if (isEdit) {
            tableColumn.setOnEditCommit(
                    (EventHandler<CellEditEvent<Map<String, String>, String>>)
                            t -> t.getRowValue().put(name, t.getNewValue()));
        }
    }

    public static void setTableColumnButonFactory(TableColumn tableColumn, String name, EventHandler<? super MouseEvent> value) {
        setTableColumnButonFactory(tableColumn, name, (mouseEvent, index) -> {
            value.handle(mouseEvent);
        });
    }

    public static void setTableColumnButonFactory(TableColumn tableColumn, String name, MouseEventCallFunc mouseEventCallFunc) {
        tableColumn.setCellFactory((col) -> {
            TableCell<Object, Boolean> cell = new TableCell<Object, Boolean>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        Button delBtn = new Button(name);
                        this.setContentDisplay(ContentDisplay.CENTER);
                        this.setGraphic(delBtn);
                        delBtn.setOnMouseClicked((me) -> {
                            mouseEventCallFunc.callFun(me, this.getIndex());
                        });
                    }
                }
            };
            return cell;
        });
    }

    public static interface MouseEventCallFunc {
        void callFun(MouseEvent mouseEvent, Integer index);
    }

    /**
     * @Title: setTableColumnMapValueFactoryAsChoiceBox
     * @Description: 初始化下拉选择表格属性
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setTableColumnMapAsChoiceBoxValueFactory(TableColumn tableColumn, String name, String[] choiceBoxStrings) {
        tableColumn.setCellValueFactory(new MapValueFactory(name));
        tableColumn.setCellFactory(
                new Callback<TableColumn<Map<String, String>, String>, TableCell<Map<String, String>, String>>() {
                    @Override
                    public TableCell<Map<String, String>, String> call(TableColumn<Map<String, String>, String> param) {
                        TableCell<Map<String, String>, String> cell = new TableCell<Map<String, String>, String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                this.setText(null);
                                this.setGraphic(null);
                                if (!empty) {
                                    ObservableList<Map<String, String>> tableData = tableColumn.getTableView().getItems();
                                    ChoiceBox<String> choiceBox = new ChoiceBox<String>();
                                    choiceBox.getItems().addAll(choiceBoxStrings);
                                    choiceBox.setValue(tableData.get(this.getIndex()).get(name));
                                    choiceBox.valueProperty().addListener((obVal, oldVal, newVal) -> {
                                        tableData.get(this.getIndex()).put(name, newVal);
                                    });
                                    this.setGraphic(choiceBox);
                                }
                            }
                        };
                        return cell;
                    }
                });
    }

    /**
     * 初始化选择框表格属性
     */
    public static void setTableColumnMapAsCheckBoxValueFactory(TableColumn tableColumn, String name) {
        tableColumn.setCellValueFactory(new MapValueFactory(name));
        tableColumn.setCellFactory(
                new Callback<TableColumn<Map<String, String>, String>, TableCell<Map<String, String>, String>>() {
                    @Override
                    public TableCell<Map<String, String>, String> call(TableColumn<Map<String, String>, String> param) {
                        TableCell<Map<String, String>, String> cell = new TableCell<Map<String, String>, String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                this.setText(null);
                                this.setGraphic(null);
                                if (!empty) {
                                    ObservableList<Map<String, String>> tableData = tableColumn.getTableView().getItems();
                                    CheckBox checkBox = new CheckBox();
                                    checkBox.setSelected(Boolean.valueOf(tableData.get(this.getIndex()).get(name)));
                                    checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
                                        tableData.get(this.getIndex()).put(name, newVal.toString());
                                    });
                                    this.setGraphic(checkBox);
                                }
                            }
                        };
                        return cell;
                    }
                });
    }
}
