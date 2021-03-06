/**
 * Sample Skeleton for 'sample.fxml' Controller Class
 */

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mozilla.universalchardet.UniversalDetector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Controller {

    @FXML
    private StackPane editStackPane;
    @FXML
    private BorderPane bpane;
    @FXML
    private StackPane root;
    @FXML
    private VBox editpane;
    @FXML
    private WebView editwebview;
    @FXML
    private JFXListView<Pane> qrlistView;
    @FXML
    private ComboBox<String> comboBoxStyle;
    @FXML
    private ComboBox<Integer> comboBoxSize;
    @FXML
    private ComboBox<Label> comboBoxFont;
    @FXML
    private ComboBox<String> comboBoxLanguage;
    @FXML
    private ComboBox<String> compileSyntax;
    @FXML
    private BorderPane editborderPane;
    @FXML
    private BorderPane QRPANEQ;
    @FXML
    private JFXTabPane tabpane;
    @FXML
    private JFXTextArea textAreaOutput;
    @FXML
    private AnchorPane compilerPane;
    @FXML
    private SplitPane compilerSplitPane;
    @FXML
    private JFXTextArea consoleInput;
    @FXML
    private StackPane consoleBar;
    @FXML
    private TextArea consoleOutput;
    @FXML
    private JFXButton compileButton;

    private static File workingSourceCodeFile;
    private static String workingSourceCodeFileEncoding;
    private JFXSnackbar snackbar;
    private String[] themes = new String[]{"xcode", "ambiance", "chaos", "chrome", "clouds", "clouds_midnight", "cobalt", "crimson_editor", "dawn", "dreamweaver", "clipse", "github", "idle_fingers", "iplastic", "katzenmilch", "kr_theme", "kuroir", "merbivore", "merbivore_soft", "mono_industrial", "monokai", "pastel_on_dark", "solarized_dark", "solarized_light", "sqlserver", "terminal", "textmate", "tomorrow", "tomorrow_night", "tomorrow_night_blue", "tomorrow_night_bright", "tomorrow_night_eighties", "twilight", "vibrant_ink"};
    private String[] languages = new String[]{"java", "c_cpp", "csharp", "python", "swift", "lua", "javascript", "html", "jsp", "php", "perl", "css", "xml", "django", "json", "assembly_x86", "fortran", "powershell", "groovy", "abap", "abc", "actionscript", "ada", "apache_conf", "applescript", "asciidoc", "autohotkey", "atchfile", "behaviour", "c9search", "cirru", "clojure", "cobol", "coffee", "coffee_worker", "coldfusion", "oldfusion_test", "css_completions", "css_test", "css_worker", "css_worker_test", "curly", "d", "dart", "diff", "dockerfile", "dot", "drools", "eiffel", "ejs", "elixir", "elm", "erlang", "forth", "ftl", "gcode", "gherkin", "gitignore", "glsl", "gobstones", "golang", "haml", "handlebars", "haskell", "haskell_cabal", "haxe", "html_completions", "html_elixir", "html_ruby", "html_test", "html_worker", "ini", "io", "jack", "jade", "json_worker", "json_worker_test", "jsoniq", "jsx", "julia", "kotlin", "latex", "less", "liquid", "lisp", "livescript", "logiql", "logiql_test", "lsl", "lua_worker", "luapage", "lucene", "makefile", "markdown", "mask", "matching_brace_outdent", "matching_parens_outdent", "matlab", "maze", "mel", "mushcode", "mysql", "nix", "nsis", "objectivec", "ocaml", "pascal", "pgsql", "php_completions", "php_test", "php_worker", "plain_text", "lain_text_test", "praat", "prolog", "properties", "protobuf", "python_test", "r", "razor", "razor_completions", "rdoc", "rhtml", "rst", "ruby", "ruby_test", "rust", "sass", "scad", "scala", "scheme", "scss", "sh", "sjs", "smarty", "snippets", "oy_template", "space", "sql", "sqlserver", "stylus", "svg", "tcl", "tex", "text", "text_test", "textile", "toml", "tsx", "twig", "typescript", "vala", "vbscript", "velocity", "verilog", "vhdl", "wollok", "xml_test", "xml_worker", "query", "xquery_worker", "yaml"};
    private JFXSpinner spinner;


    public static int IMAGE_HEIGHT = 600;
    public static int IMAGE_WIDTH = 600;
    public static boolean isNetworkAvailable;
    public static final int SAVE_CODE_STATUS_SUCCESS = 0;
    public static final int SAVE_CODE_STATUS_SUCCESS_ANOTHER = 5;
    public static final int SAVE_CODE_STATUS_CANCELED = 1;
    public static final int SAVE_CODE_STATUS_FAILED = 2;
    public static final int SAVE_MODE_ANOTHER = 3;
    public static final int SAVE_MODE_OVERWRITE = 4;
    private static int compile_used_language = 0;
    private JFXDialog closeDialog;
    private JFXDialog aboutDialog;
    private List<Pair<String, Integer>> availableLanguages;
    private JFXDialog compileDialog;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Stage stage;

    @FXML
    void initialize() {
        availableLanguages = null;
        workingSourceCodeFile = null;
        workingSourceCodeFileEncoding = null;
        spinner = new JFXSpinner();
        snackbar = new JFXSnackbar(editpane);
        snackbar.setPrefWidth(300);
        snackbar.getStylesheets().add(getClass().getResource("snackbar_styles.css").toExternalForm());
        snackbar.setId("snackbar");
        snackbar.setStyle("-fx-text-fill: #FFFFFF;");
        editwebview.getEngine().setJavaScriptEnabled(true);
        editwebview.getEngine().load(getClass().getResource("src-noconflict/index.html").toExternalForm());
         /*editwebview.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            public void changed(ObservableValue<? extends Document> prop, Document oldDoc, Document newDoc) {
                enableFirebug(editwebview.getEngine());
            }
        });*/
        try {
            if (InetAddress.getByName("176.119.35.132").isReachable(3000)) {
                isNetworkAvailable = true;
                System.out.println("getting...");
                availableLanguages = QRHttpUtils.getAvailableLanguages();
                for (Pair<String, Integer> p : availableLanguages) {
                    compileSyntax.getItems().add(p.getLeft());
                }
                compileSyntax.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        compile_used_language = compileSyntax.getSelectionModel().getSelectedIndex();
                    }
                });
                compileSyntax.getSelectionModel().selectFirst();
            } else {
                compilerSplitPane.getItems().remove(compilerPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        setListenerEditWebView(editwebview);
        setupcomboBoxStyle(comboBoxStyle);
        setupComboBoxSize(comboBoxSize);
        setupcomboBoxFont(comboBoxFont);
        setupQRListView(qrlistView);
        setupcomboBoxLanguage(comboBoxLanguage);
    }


    @FXML
    void openSourceCode(ActionEvent event) {
        System.out.println("OPEN");
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("소스 코드", "*.c", "*.cpp", "*.java", "*.php", "*.asp", "*.html", "*.htm", "*.js", "*.css", "*.py", "*.sh", "*.rua", "*.jsp", "*.pl", "*.fs", "*.bas", "*.ss", "*.s", "*.swift", "*.cc", "*.pdml", "*.lss", "*.lsp", "*.cp", "*.phps", "*.txt"));
        final File sourceCodeFile = chooser.showOpenDialog(editwebview.getParent().getScene().getWindow());
        loadSourceCodeFile(sourceCodeFile);
    }

    private void loadSourceCodeFile(final File sourceCodeFile) {
        if (sourceCodeFile != null && eraseSourceCode()) {
            workingSourceCodeFile = sourceCodeFile;
            editborderPane.centerProperty().set(spinner);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        byte[] buf = new byte[4096];
                        FileInputStream fis = new FileInputStream(sourceCodeFile);
                        UniversalDetector detector = new UniversalDetector(null);
                        int nread;
                        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                            detector.handleData(buf, 0, nread);
                        }
                        detector.dataEnd();
                        workingSourceCodeFileEncoding = detector.getDetectedCharset();
                        if (workingSourceCodeFileEncoding != null) {
                            System.out.println("Detected Encoding = " + workingSourceCodeFileEncoding);
                        } else {
                            System.out.println("No Encoding detected.");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        final String value = FileUtils.readFileToString(workingSourceCodeFile, workingSourceCodeFileEncoding);
                        Platform.runLater(new Runnable() {
                            public void run() {
                                editwebview.getEngine().executeScript("editor.setValue('" + StringEscapeUtils.escapeEcmaScript(value) + "')");
                                stage.setTitle(workingSourceCodeFile != null ? new String("QR Studio 1.0 - [" + workingSourceCodeFile.getAbsolutePath() + "] - [" + workingSourceCodeFile.getName() + "]") : "QR Studio 1.0");
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                editborderPane.centerProperty().set(editwebview);
                            }
                        });
                    }
                }
            }).start();
        }
    }

    @FXML
    void saveSource(ActionEvent event) {
        int respondCode = saveSourceCode(SAVE_MODE_OVERWRITE);
        switch (respondCode) {
            case SAVE_CODE_STATUS_SUCCESS:
                snackbar.show("저장하였습니다.", 2000);
                break;
            case SAVE_CODE_STATUS_SUCCESS_ANOTHER:
                snackbar.show(workingSourceCodeFile.getAbsolutePath() + "로 저장하였습니다.", 2000);
                break;
            case SAVE_CODE_STATUS_FAILED:
                snackbar.show("저장하는데 실패하였습니다.", 2000);
                break;
            default:
                break;
        }
    }

    @FXML
    void createQRCodeCurrent(ActionEvent event) {
        createQRCode(null);
    }

    @FXML
    void saveAnotherSource(ActionEvent event) {
        int respondCode = saveSourceCode(SAVE_MODE_ANOTHER);
        switch (respondCode) {
            case SAVE_CODE_STATUS_SUCCESS:
                snackbar.show("저장하였습니다.", 2000);
                break;
            case SAVE_CODE_STATUS_SUCCESS_ANOTHER:
                snackbar.show(workingSourceCodeFile.getAbsolutePath() + "로 저장하였습니다.", 2000);
                break;
            case SAVE_CODE_STATUS_FAILED:
                snackbar.show("저장하는데 실패하였습니다.", 2000);
                break;
            default:
                break;
        }
    }

    @FXML
    void compileSource(ActionEvent event) {
        final String sourceCodeContent = getSourceCodeValueFromEditor();
        final int compileLanguageCode = availableLanguages.get(compile_used_language).getRight();
        String tempCompileInputContent = consoleInput.getText();
        tempCompileInputContent = (tempCompileInputContent == null || tempCompileInputContent.isEmpty()) ? "" : tempCompileInputContent;
        final String compileInputContent = tempCompileInputContent;
        final JFXSpinner spinner = new JFXSpinner();
        final Label compileLabel = new Label("\n\n\n준비중...");
        consoleBar.getChildren().add(spinner);
        consoleBar.getChildren().add(compileLabel);
        consoleBar.setDisable(true);
        compileButton.setDisable(true);
        new Thread(new Runnable() {
            public void run() {
                String submissionQuery = QRHttpUtils.createSubmissionQuery(sourceCodeContent, compileLanguageCode, compileInputContent);
                System.out.println(submissionQuery);
                try {
                    String respondId = QRHttpUtils.postSubmissionQuery(submissionQuery);
                    JSONObject object = (JSONObject) new JSONParser().parse(respondId);
                    System.out.println(object.get("id"));
                    String result;
                    while (true) {
                        result = QRHttpUtils.getSubmissionResult(object.get("id").toString());
                        double status = QRHttpUtils.getProgessStatus(result);
                        if (status < 0) {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    compileLabel.setText("\n\n\n준비중...");
                                }
                            });
                        }
                        if (status == 1) {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    compileLabel.setText("\n\n\n컴파일중....");
                                }
                            });
                        }
                        if (status == 3) {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    compileLabel.setText("\n\n\n실행중....");
                                }
                            });
                        }
                        if (status == 0) {
                            break;
                        }
                        Thread.sleep(100);
                    }
                    System.out.println(result);
                    final QRHttpUtils.SubmissionDetail detail = new QRHttpUtils.SubmissionDetail(result);
                    Platform.runLater(new Runnable() {
                        public void run() {
                            consoleOutput.setText(detail.getOutputOrError());
                            consoleBar.getChildren().remove(spinner);
                            consoleBar.getChildren().remove(compileLabel);
                            consoleBar.setDisable(false);
                            compileButton.setDisable(false);
                        }
                    });
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @FXML
    void openQRCodeImage(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("QR코드가 포함된 이미지", "*.png", "*.jpg", "*.bmp", "*.gif"));
            File tempFile = fileChooser.showOpenDialog(editwebview.getParent().getScene().getWindow());
            if (tempFile != null) {
                Image image = new Image(tempFile.toURI().toURL().toExternalForm());
                if (image != null) {
                    try {
                        String decoded = QRCodeUtils.DecodeToImage(image);
                        String decompressed = CompressUtils.decompressText(CompressUtils.removeMarker(decoded));
                        System.out.println(decompressed);
                        editwebview.getEngine().executeScript("editor.setValue('" + StringEscapeUtils.escapeEcmaScript(decompressed) + "')");
                        stage.setTitle("QR Studio 1.0 - [" + tempFile.getAbsolutePath() + "] - [" + tempFile.getName() + "]");
                        qrlistView.getItems().add(0, getQRCodeListItem(decompressed, image, tempFile.getName(), tempFile.getAbsolutePath()));
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                        snackbar.show("QR코드를 인식하지 못하였습니다. 더 선명한 사진을 이용하세요.", 2000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ChecksumException e) {
                        e.printStackTrace();
                    } catch (FormatException e) {
                        snackbar.show("QR코드를 인식하지 못하였습니다. 더 선명한 사진을 이용하세요.", 2000);
                        e.printStackTrace();
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void saveQRCodeToFile() {
        try {
            Pane pane = qrlistView.getSelectionModel().getSelectedItem();
            ImageView qrImageView = (ImageView) pane.lookup("#itemImageView");
            Image image = qrImageView.getImage();
            if (image != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("이미지", "*.png", "*.jpg", "*.bmp", "*.gif"));
                File tempFile = fileChooser.showSaveDialog(editwebview.getParent().getScene().getWindow());
                if (tempFile != null) {
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                    ImageIO.write(bufferedImage, FilenameUtils.getExtension(tempFile.getName()), tempFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pane getQRCodeListItem(String content, final Image image, String filename, String absoluteFilePath) {
        try {
            final Pane itemRoot = new FXMLLoader(getClass().getResource("qritem.fxml")).load();
            ImageView imageView = (ImageView) itemRoot.lookup("#itemImageView");
            VBox vBox = (VBox) itemRoot.lookup("#itemVBox");
            Label projectName = (Label) itemRoot.lookup("#itemProjectName");
            Label dateTime = (Label) itemRoot.lookup("#itemDateTime");
            Label thumbnail = (Label) itemRoot.lookup("#itemContentThumbnail");
            Label filePath = (Label) itemRoot.lookup("#itemContentFilePath");
            //final JFXPopup popup = (JFXPopup) root.lookup("#itemPopup");
            imageView.setImage(image);
            if (filename != null && !filename.isEmpty()) {
                projectName.setText(filename);
                filePath.setText(absoluteFilePath);
            } else {
                if (workingSourceCodeFile != null) {
                    projectName.setText(workingSourceCodeFile.getName());
                    filePath.setText(workingSourceCodeFile.getAbsolutePath());
                } else {
                    projectName.setText("No Title");
                    vBox.getChildren().remove(filePath);
                }
            }
            dateTime.setText(workingSourceCodeFile != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(workingSourceCodeFile.lastModified()) : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            if (content.length() < 100) {
                thumbnail.setText(content.isEmpty() ? "내용 없음" : content);
            } else {
                thumbnail.setText(content.substring(0, 100));
            }
            itemRoot.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    if (event.isPopupTrigger()) {   //마우스 오른쪽 클릭
                        final JFXPopup popup = new JFXPopup();
                        VBox vBox = new VBox();
                        vBox.setStyle("-fx-background-color:#FFFFFF");
                        JFXButton open = new JFXButton("  화면에 열기  ");
                        open.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent event) {
                                loadQRCodeToEditor();
                                popup.close();
                            }
                        });
                        open.setPadding(new Insets(10));
                        JFXButton save = new JFXButton("사진으로 저장");
                        save.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent event) {
                                saveQRCodeToFile();
                                popup.close();
                            }
                        });
                        save.setPadding(new Insets(10));
                        JFXButton delete = new JFXButton("목록에서 삭제");
                        delete.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent event) {
                                qrlistView.getItems().remove(qrlistView.getSelectionModel().getSelectedIndex());
                                popup.close();
                            }
                        });
                        delete.setPadding(new Insets(10));

                        JFXButton showBig = new JFXButton("    크게 보기    ");
                        showBig.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent event) {
                                JFXDialogLayout content = new JFXDialogLayout();
                                ImageView qrImageView = new ImageView(image);
                                content.setBody(qrImageView);
                                final JFXDialog qrdialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
                                JFXButton button = new JFXButton("닫기");
                                button.setStyle("-fx-text-fill: #2196F3;");
                                button.setOnAction(new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent event) {
                                        qrdialog.close();
                                    }
                                });
                                content.setActions(button);
                                qrdialog.show();
                                popup.close();
                            }
                        });
                        showBig.setPadding(new Insets(10));
                        vBox.getChildren().add(open);
                        vBox.getChildren().add(save);
                        vBox.getChildren().add(delete);
                        vBox.getChildren().add(showBig);
                        popup.setContent(vBox);
                        popup.setSource(itemRoot);
                        popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, event.getX(), event.getY());
                    } else {
                        loadQRCodeToEditor();
                    }
                }
            });
            return itemRoot;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    void createQRCode(ActionEvent event) {
        final String content = getSourceCodeValueFromEditor();
        new Thread(new Runnable() {
            public void run() {
                try {
                    byte[] compressedByte = CompressUtils.compressText(content);
                    final String compressedText = CompressUtils.addMarker(compressedByte);
                    final Image image = QRCodeUtils.EncodeToQRCode(compressedText, IMAGE_WIDTH, IMAGE_HEIGHT);
                    Platform.runLater(new Runnable() {
                        public void run() {
                            if (compressedText.length() < 2900) {
                                qrlistView.getItems().add(0, getQRCodeListItem(content, image, null, null));
                            } else {
                                snackbar.show("문자가 너무커 QR코드로 만들지 못하였습니다.", 2000);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void loadQRCodeToEditor() {
        eraseSourceCode();
        Pane pane = qrlistView.getSelectionModel().getSelectedItem();
        ImageView qrImageView = (ImageView) pane.lookup("#itemImageView");
        Image image = qrImageView.getImage();
        if (image != null) {
            try {
                String decoded = QRCodeUtils.DecodeToImage(image);
                String decompressed = CompressUtils.decompressText(CompressUtils.removeMarker(decoded));
                System.out.println(decompressed);
                editwebview.getEngine().executeScript("editor.setValue('" + StringEscapeUtils.escapeEcmaScript(decompressed) + "')");
            } catch (NotFoundException e) {
                e.printStackTrace();
                snackbar.show("QR코드를 인식하지 못하였습니다. 더 선명한 사진을 이용하세요.", 2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ChecksumException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                snackbar.show("QR코드를 인식하지 못하였습니다. 더 선명한 사진을 이용하세요.", 2000);
                e.printStackTrace();
            }
        }
    }

    private int saveSourceCode(int saveMode) {
        try {
            if (workingSourceCodeFile == null || saveMode == SAVE_MODE_ANOTHER) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("소스 코드", "*.cpp", "*.c", "*.java", "*.php", "*.asp", "*.html", "*.htm", "*.js", "*.css", "*.py", "*.sh", "*.rua", "*.jsp", "*.pl", "*.fs", "*.bas", "*.ss", "*.s", "*.swift", "*.cc", "*.pdml", "*.lss", "*.lsp", "*.cp", "*.phps", "*.txt"));
                File tempFile = fileChooser.showSaveDialog(editwebview.getParent().getScene().getWindow());
                if (tempFile == null) {
                    return SAVE_CODE_STATUS_CANCELED;
                }
                workingSourceCodeFile = tempFile;
                workingSourceCodeFileEncoding = null;
                if (!workingSourceCodeFile.exists()) {
                    workingSourceCodeFile.createNewFile();
                }
            }
            System.out.println("Saved as:" + workingSourceCodeFileEncoding);
            FileUtils.writeStringToFile(workingSourceCodeFile, getSourceCodeValueFromEditor(), false);
            stage.setTitle(workingSourceCodeFile != null ? new String("QR Studio 1.0 - [" + workingSourceCodeFile.getAbsolutePath() + "] - [" + workingSourceCodeFile.getName() + "]") : "QR Studio 1.0");

        } catch (IOException e) {
            e.printStackTrace();
            return SAVE_CODE_STATUS_FAILED;
        }
        return saveMode == SAVE_MODE_ANOTHER ? SAVE_CODE_STATUS_SUCCESS_ANOTHER : SAVE_CODE_STATUS_SUCCESS;
    }

    private String getSourceCodeValueFromEditor() {
        String sourceCodeValue = (String) editwebview.getEngine().executeScript("editor.getValue()");
        return sourceCodeValue;
    }

    private void setListenerEditWebView(final WebView editwebview) {
        editwebview.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasFiles()) {
                    for (File file : dragboard.getFiles()) {
                        if (FilenameUtils.isExtension(file.getName(), new String[]{"c", "cpp", "java", "php", "asp", "html", "htm", "js", "css", "py", "sh", "rua", "jsp", "pl", "fs", "bas", "ss", "s", "swift", "cc", "pdml", "lss", "lsp", "cp", "phps", "txt"})) {
                            try {
                                String encoding = "";
                                byte[] buf = new byte[4096];
                                FileInputStream fis = new FileInputStream(file);
                                UniversalDetector detector = new UniversalDetector(null);
                                int nread;
                                while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                                    detector.handleData(buf, 0, nread);
                                }
                                detector.dataEnd();
                                encoding = detector.getDetectedCharset();
                                if (encoding != null) {
                                    System.out.println("Detected Encoding = " + encoding);
                                } else {
                                    System.out.println("No Encoding detected.");
                                }
                                editwebview.getEngine().executeScript("editor.insert('" + StringEscapeUtils.escapeEcmaScript(FileUtils.readFileToString(file, encoding)) + "')");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                event.consume();
            }
        });
        editwebview.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.isControlDown() && event.getCode() == KeyCode.V) {
                    final Clipboard clipBoard = Clipboard.getSystemClipboard();
                    String content = (String) clipBoard.getContent(DataFormat.PLAIN_TEXT);
                    if (content == null) {
                        return;
                    }
                    content = StringEscapeUtils.escapeEcmaScript(content);
                    String query = "editor.insert('" + content + "');";
//                    TODO 빌드할때는 이거 꼭 지우기
//                    editwebview.getEngine().executeScript(query);
                }
                if (event.getCode() == KeyCode.ESCAPE) {
                    pressedEscape();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.S) {
                    int respondCode = saveSourceCode(workingSourceCodeFile == null ? SAVE_MODE_ANOTHER : SAVE_MODE_OVERWRITE);
                    switch (respondCode) {
                        case SAVE_CODE_STATUS_SUCCESS:
                            snackbar.show("저장하였습니다.", 2000);
                            break;
                        case SAVE_CODE_STATUS_SUCCESS_ANOTHER:
                            snackbar.show(workingSourceCodeFile.getAbsolutePath() + "로 저장하였습니다.", 2000);
                            break;
                        case SAVE_CODE_STATUS_FAILED:
                            snackbar.show("저장하는데 실패하였습니다.", 2000);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        editwebview.setOnScroll(new EventHandler<ScrollEvent>() {
            public void handle(ScrollEvent event) {
                if (event.isControlDown() && event.getDeltaY() < 0) {
                    System.out.println("down");
                    if (comboBoxSize.getSelectionModel().getSelectedIndex() > 0) {
                        comboBoxSize.getSelectionModel().select(comboBoxSize.getSelectionModel().getSelectedIndex() - 1);
                        int size = comboBoxSize.getSelectionModel().getSelectedItem();
                        System.out.println("document.getElementById('editor').style.fontSize='" + size + "px';");
                        editwebview.getEngine().executeScript("document.getElementById('editor').style.fontSize='" + size + "px';");
                    }
                }
                if (event.isControlDown() && event.getDeltaY() > 1) {
                    System.out.println("up");
                    if (comboBoxSize.getSelectionModel().getSelectedIndex() < comboBoxSize.getItems().size() - 1) {
                        comboBoxSize.getSelectionModel().select(comboBoxSize.getSelectionModel().getSelectedIndex() + 1);
                        int size = comboBoxSize.getSelectionModel().getSelectedItem();
                        System.out.println("document.getElementById('editor').style.fontSize='" + size + "px';");
                        editwebview.getEngine().executeScript("document.getElementById('editor').style.fontSize='" + size + "px';");
                    }
                }
            }
        });
    }

    private void setupComboBoxSize(final ComboBox<Integer> comboBoxSize) {
        for (int i = 0; i < 11; i++) {
            this.comboBoxSize.getItems().add(10 + i * 2);
        }
        comboBoxSize.getSelectionModel().select(3);
        comboBoxSize.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                int size = comboBoxSize.getSelectionModel().getSelectedItem();
                System.out.println("document.getElementById('editor').style.fontSize='" + size + "px';");
                editwebview.getEngine().executeScript("document.getElementById('editor').style.fontSize='" + size + "px';");
            }
        });
    }

    private void setupcomboBoxStyle(final ComboBox<String> comboBoxStyle) {
        for (int i = 0; i < themes.length; i++) {
            this.comboBoxStyle.getItems().add(themes[i]);
        }
        comboBoxStyle.getSelectionModel().selectFirst();
        comboBoxStyle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String theme = comboBoxStyle.getSelectionModel().getSelectedItem();
                System.out.println("editor.setTheme(\"ace/theme/" + theme + "\");");
                editwebview.getEngine().executeScript("editor.setTheme(\"ace/theme/" + theme + "\");");
            }
        });

    }

    private void setupcomboBoxFont(final ComboBox<Label> comboBoxFont) {
        for (int i = 0; i < Font.getFamilies().size(); i++) {
            Label label = new Label(Font.getFamilies().get(i));
            label.setFont(Font.font(Font.getFamilies().get(i)));
            label.setStyle("-fx-text-fill: rgb(100.0, 100.0, 100.0)");
            comboBoxFont.getItems().add(label);
        }
        comboBoxFont.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String fontfamily = comboBoxFont.getSelectionModel().getSelectedItem().getText();
                editwebview.getEngine().executeScript("editor.setOptions({fontFamily: \"" + fontfamily + "\"});");
            }
        });
    }

    private void setupcomboBoxLanguage(final ComboBox<String> comboBoxLanguage) {
        for (int i = 0; i < languages.length; i++) {
            this.comboBoxLanguage.getItems().add(languages[i].toUpperCase());
        }
        comboBoxLanguage.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String program_language = comboBoxLanguage.getSelectionModel().getSelectedItem();
                System.out.println("editor.getSession().setMode(\"ace/mode/" + program_language.toLowerCase() + "\");\n");
                editwebview.getEngine().executeScript("editor.getSession().setMode(\"ace/mode/" + program_language.toLowerCase() + "\");\n");
            }
        });
    }

    private void setupQRListView(final JFXListView<Pane> qrlistView) {
        this.qrlistView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                final JFXPopup popup = new JFXPopup();
                VBox vBox = new VBox();
                vBox.setStyle("-fx-background-color:#FFFFFF");
                JFXButton open = new JFXButton("QR코드 가져오기");
                open.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        openQRCodeImage(null);
                        popup.close();
                    }
                });
                open.setPadding(new Insets(10));
                vBox.getChildren().add(open);
                popup.setContent(vBox);
                popup.setSource(qrlistView);
                popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, event.getX(), event.getY());
            }
        });

        this.qrlistView.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getGestureSource() != qrlistView &&
                        event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        this.qrlistView.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasFiles()) {
                    for (File file : dragboard.getFiles()) {
                        if (FilenameUtils.isExtension(file.getName(), new String[]{"png", "jpg", "gif", "bmp"})) {
                            try {
                                Image image = new Image(file.toURI().toURL().toExternalForm());
                                if (image != null) {
                                    try {
                                        String decoded = QRCodeUtils.DecodeToImage(image);
                                        String decompressed = CompressUtils.decompressText(CompressUtils.removeMarker(decoded));
                                        System.out.println(decompressed);
                                        editwebview.getEngine().executeScript("editor.setValue('" + StringEscapeUtils.escapeEcmaScript(decompressed) + "')");
                                        stage.setTitle("QR Studio 1.0 - [" + file.getAbsolutePath() + "] - [" + file.getName() + "]");
                                        qrlistView.getItems().add(0, getQRCodeListItem(decompressed, image, file.getName(), file.getAbsolutePath()));
                                    } catch (NotFoundException e) {
                                        e.printStackTrace();
                                        snackbar.show("QR코드를 인식하지 못하였습니다. 더 선명한 사진을 이용하세요.", 2000);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (ChecksumException e) {
                                        e.printStackTrace();
                                    } catch (FormatException e) {
                                        snackbar.show("QR코드를 인식하지 못하였습니다. 더 선명한 사진을 이용하세요.", 2000);
                                        e.printStackTrace();
                                    }
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            snackbar.show("이미지 파일을 가져오시기 바랍니다.", 2000);
                        }
                    }
                }
                event.consume();
            }
        });
    }

    private boolean eraseSourceCode() {
        try {
            editwebview.getEngine().executeScript("editor.setValue('');  editor.getSession().setUndoManager(new ace.UndoManager());");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void enableFirebug(final WebEngine engine) {
        editwebview.getEngine().executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
    }

    @FXML
    void about(ActionEvent event) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("QR Studio에 오신것을 환영합니다."));
        ((Text) content.getHeading().get(0)).setFont(Font.font("Roboto", 17));
        content.setBody(new Text("이 프로그램은 작성한 소스코드를 QR코드, 혹은 QR코드 묶음으로 변환하여 \n취급, 전달, 공유를 간편하게 만든게 특징입니다.\n\n온라인 컴파일러를 사용하여 어디에나, 어느 컴퓨터, 어느 운영체제에서든\n컴파일 및 실행이 가능하게하는 기능을 제공합니다.\n\nQR코드를 활용한 새로운 세대의 에디터를 사용해보세요!\n\nCreated By: 정승욱"));
        ((Text) content.getBody().get(0)).setFont(Font.font("Roboto", 12));
        aboutDialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("확인");
        button.setStyle("-fx-text-fill: #2196F3;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                aboutDialog.close();
            }
        });
        content.setActions(button);
        aboutDialog.show();
        editwebview.requestFocus();
    }

    @FXML
    void openComplexSetting(ActionEvent event) {
        try {
            editwebview.requestFocus();
            new Robot().keyPress(java.awt.event.KeyEvent.VK_CONTROL);
            new Robot().keyPress(java.awt.event.KeyEvent.VK_COMMA);
            new Robot().keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
            new Robot().keyRelease(java.awt.event.KeyEvent.VK_COMMA);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void closeQRCompiler(ActionEvent event) {
        try {
            if (!((workingSourceCodeFile == null && (getSourceCodeValueFromEditor() == null || getSourceCodeValueFromEditor().isEmpty() || getSourceCodeValueFromEditor().length() <= 0)) || (workingSourceCodeFile != null && FileUtils.readFileToString(workingSourceCodeFile, workingSourceCodeFileEncoding).equals(getSourceCodeValueFromEditor())))) {
                JFXDialogLayout content = new JFXDialogLayout();
                if (workingSourceCodeFile != null) {
                    content.setHeading(new Text("'" + workingSourceCodeFile.getName() + "'에서 수정한 내용을 저장하겠습니까?"));
                } else {
                    content.setHeading(new Text("작성하신 내용을 저장하겠습니까?"));
                }
                ((Text) content.getHeading().get(0)).setFont(Font.font("Roboto", 17));
                content.setBody(new Text("저장하지 않으면 수정사항이 사라집니다."));
                ((Text) content.getBody().get(0)).setFont(Font.font("Roboto", 12));
                if (closeDialog != null) {
                    closeDialog.close();
                }
                closeDialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
                JFXButton save = new JFXButton("저장");
                save.setStyle("-fx-text-fill: #2196F3;");
                save.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        int respondCode = saveSourceCode(SAVE_MODE_OVERWRITE);
                        switch (respondCode) {
                            case SAVE_CODE_STATUS_SUCCESS:
                                System.exit(0);
                                break;
                            case SAVE_CODE_STATUS_SUCCESS_ANOTHER:
                                System.exit(0);
                                break;
                            case SAVE_CODE_STATUS_FAILED:
                                snackbar.show("저장하는데 실패하였습니다.", 2000);
                                break;
                            case SAVE_CODE_STATUS_CANCELED:
                                break;
                        }
                    }
                });
                JFXButton dontsave = new JFXButton("저장하지 않기");
                dontsave.setStyle("-fx-text-fill: #2196F3;");
                dontsave.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        System.exit(0);
                    }
                });
                JFXButton cancel = new JFXButton("취소");
                cancel.setStyle("-fx-text-fill: #2196F3;");
                cancel.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        closeDialog.close();
                        editwebview.requestFocus();
                    }
                });
                content.setActions(save, dontsave, cancel);
                closeDialog.show();
                editwebview.requestFocus();
            } else {
                JFXDialogLayout content = new JFXDialogLayout();
                content.setHeading(new Text("정말로 닫으시겠습니까?"));
                ((Text) content.getHeading().get(0)).setFont(Font.font("Roboto", 17));
                content.setBody(new Text("  "));
                ((Text) content.getBody().get(0)).setFont(Font.font("Roboto", 12));
                if (closeDialog != null) {
                    closeDialog.close();
                }
                closeDialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
                JFXButton button = new JFXButton("확인");
                button.setStyle("-fx-text-fill: #2196F3;");
                button.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        System.exit(0);
                    }
                });

                JFXButton cancel = new JFXButton("취소");
                cancel.setStyle("-fx-text-fill: #2196F3;");
                cancel.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        closeDialog.close();
                        editwebview.requestFocus();
                    }
                });
                content.setActions(button, cancel);
                closeDialog.show();
                editwebview.requestFocus();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void pressedEscape() {
        if (closeDialog != null) {
            closeDialog.close();
        }
        if (aboutDialog != null) {
            aboutDialog.close();
        }
    }

    public void setQRCodeSize(ActionEvent event) {
        try {
            final VBox vBox = new FXMLLoader(getClass().getResource("qrsize.fxml")).load();
            JFXDialogLayout content = new JFXDialogLayout();
            content.setBody(vBox);
            final JFXDialog qrdialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
            final JFXTextField width = (JFXTextField) vBox.lookup("#qrsizewidth");
            final JFXTextField height = (JFXTextField) vBox.lookup("#qrsizeheight");
            width.setText(String.valueOf(IMAGE_WIDTH));
            height.setText(String.valueOf(IMAGE_HEIGHT));
            width.textProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        width.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
            height.textProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        height.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
            JFXButton ok = new JFXButton("확인");
            ok.setStyle("-fx-text-fill: #2196F3;");
            ok.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    int inputWidth = Integer.parseInt(width.getText());
                    int inputHeight = Integer.parseInt(height.getText());
                    if (inputHeight > 0 && inputWidth > 0 && inputHeight < 3000 && inputWidth < 3000) {
                        IMAGE_HEIGHT = inputHeight;
                        IMAGE_WIDTH = inputWidth;
                        snackbar.show("너비 " + inputWidth + " 높이 " + inputHeight + " 으로 지정하였습니다.", 2000);
                    } else {
                        snackbar.show("값이 적절하지 않습니다. 0이상 3000이하의 크기로 정해주십시오.", 2000);
                    }
                    qrdialog.close();
                }
            });
            JFXButton cancel = new JFXButton("취소");
            cancel.setStyle("-fx-text-fill: #2196F3;");
            cancel.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    qrdialog.close();
                }
            });
            content.setActions(ok, cancel);
            qrdialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
