package scenario.test;

import static com.codeborne.selenide.Selenide.*;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.conditions.Text;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class SwaggerEditorPage {

    /**
     * @param url
     */
    public SwaggerEditorPage(String url) {
        open(url);
    }

    /**
     * @param label
     * @throws Exception
     */
    public void screenshot(String label) throws Exception {
        Screenshot shot = new AShot()
                          .shootingStrategy(ShootingStrategies.viewportPasting(100))
                          .takeScreenshot(WebDriverRunner.getWebDriver());
        File f = new File("build/reports/tests/scenario/test/" + SwaggerEditorPage.class.getSimpleName() + "_" + label + ".png") ;
        f.mkdirs();
        ImageIO.write(shot.getImage(), "png",f);
    }
    /**
     * @param label
     * @throws Exception
     */
    public void elementScreenshot(String label,String elementName) throws Exception {
        String elementCss = null;
        if ("pane1".equals(elementName)) {
            elementCss = "#swagger-editor > div > div:nth-child(2) > section > div > div.Pane.vertical.Pane1";
        } else if ("pane2".equals(elementName)) {
            elementCss = "#swagger-editor > div > div:nth-child(2) > section > div > div.Pane.vertical.Pane2";
        } else {
            throw new RuntimeException("No Such Element");
        }
        // FIXED AShotだと、以下エラー。デフォルトjqueryを使うようなので、CoordsProviderの指定が必要
        // 指定しても、windows版 chromeではちゃんと取れない。docker上ではok
        // また、全体スクリーンショットの場合、指定すると全画面のスクリーンショットが取れない
        // org.openqa.selenium.WebDriverException: unknown error: $ is not defined
        // => https://github.com/yandex-qatools/ashot/issues/68
        // use aShot.coordsProvider(new WebDriverCoordsProvider()) if you don't have jQuery on your page.
        // WebDriverCoordsProvider not uses jQuery for position calculating.
        Screenshot shot = new AShot().coordsProvider(new WebDriverCoordsProvider())
                                      .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(WebDriverRunner.getWebDriver(),
                                By.cssSelector(elementCss).findElement(WebDriverRunner.getWebDriver()));
        BufferedImage image = shot.getImage();

        File file = new File("build/reports/tests/scenario/test/" + SwaggerEditorPage.class.getSimpleName() + "_" + label + ".png") ;
        file.mkdirs();
        ImageIO.write(image, "png",file);
    }


    /**
     * @param label
     */
    public void click(String label) {
        String elementCss = null;
        if ("User:".equals(label)) {
            elementCss = "#swagger-editor > div > div:nth-child(1) > div.topbar > div > div.topbar-specmgr-info > div:nth-child(4) > span";
        } else if ("Add User".equals(label)) {
            elementCss = "#swagger-editor > div > div:nth-child(1) > div.topbar > div > div.topbar-specmgr-info > div:nth-child(4) > div > ul > li:nth-child(1) > button";
        } else if ("Create".equals(label)) {
            elementCss = "#swagger-editor > div > div:nth-child(1) > div.topbar > div > div.topbar-specmgr-info > span:nth-child(5) > div.swagger-ui > div > div > div > div > div > div.modal-ux-content > div.topbar-popup-button-area > button";
        } else if ("Change User".equals(label)) {
            elementCss = "#swagger-editor > div > div:nth-child(1) > div.topbar > div > div.topbar-specmgr-info > div:nth-child(4) > div > ul > li:nth-child(3) > button";
        } else {
            throw new RuntimeException("No Such Label");
        }
        $(By.cssSelector(elementCss)).click();
    }


    /**
     * @param label
     * @param value
     */
    public void input(String label, String value) {
        if ("id:".equals(label)) {
            $(By.cssSelector("#swagger-editor > div > div:nth-child(1) > div.topbar > div > div.topbar-specmgr-info > span:nth-child(5) > div.swagger-ui > div > div > div > div > div > div.modal-ux-content > div.parameters-col_description > div:nth-child(2) > section > input[type=\"text\"]")).setValue(value);
        } else if ("email:".equals(label)) {
            $(By.cssSelector("#swagger-editor > div > div:nth-child(1) > div.topbar > div > div.topbar-specmgr-info > span:nth-child(5) > div.swagger-ui > div > div > div > div > div > div.modal-ux-content > div.parameters-col_description > div:nth-child(3) > section > input[type=\"text\"]")).setValue(value);
        } else if ("ace-editor".equals(label)) {
            // 不細工。もうちょっとうまく細工を...
            for (String val : value.split("\\\\n")) {
                $(By.cssSelector( "#ace-editor > textarea")).sendKeys(val);
                $(By.cssSelector( "#ace-editor > textarea")).sendKeys(Keys.RETURN);
            }
            // FIXME ace エディタへの入力は sendKeyでないとうまくいかない..
            // Element should be visible {#ace-editor > textarea}
            // https://stackoverflow.com/questions/30972119/how-to-automate-the-ace-editor-send-keys-using-webdriver
            // executeJavaScript("arguments[0].value = arguments[1];", $(By.cssSelector("#ace-editor > textarea")).toWebElement(), value);
        } else {
            throw new RuntimeException("No Such Label");
        }
    }

    /**
     * @param title
     */
    public void checkDialog(String title) {
        if ("Add User".equals(title)) {
            $(By.cssSelector("#swagger-editor > div > div:nth-child(1) > div.topbar > div > div.topbar-specmgr-info > span:nth-child(5) > div.swagger-ui > div > div > div > div > div > div.modal-ux-header > h3")).shouldHave(new Text(title));
        } else {
            throw new RuntimeException("No Such Label");
        }
    }

}