package scenario.test;

import static com.codeborne.selenide.Selenide.*;

import org.openqa.selenium.By;

import com.codeborne.selenide.Selenide;

public class GitlabProjectDetailPage extends PageObj {

    public GitlabProjectDetailPage() {
    }

    /**
     * @param label
     */
    public PageObj click(String label) {
        PageObj page = this;
        String elementCss = null;
        if ("Settings".equals(label)) {
            elementCss = "body > div.page-with-contextual-sidebar.page-with-sidebar.right-sidebar-collapsed > div.nav-sidebar > div > ul > li:nth-child(7) > a";
        } else if ("Remove project".equals(label)) {
            elementCss = "#content-body > div.container-fluid.container-limited > div.empty_wrapper > div > fieldset:nth-child(4) > div > a";
            page = new GitlabProjectPage();
        } else {
            throw new RuntimeException("No Such Label");
        }
        $(By.cssSelector(elementCss)).click();
        if ("Remove project".equals(label)) {
            Selenide.switchTo().alert().accept();
        }
        return page;
    }

    /**
     * @param label
     * @param value
     */
    public void input(String label, String value) {
        if ("Confirm input".equals(label)) {
            $(By.cssSelector("#confirm_name_input")).setValue(value);
        } else {
            throw new RuntimeException("No Such Label");
        }
    }

}