package eu.arima.fileencodingdetection;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.validation.constraints.NotBlank;

import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/fileencoding")
public class FileEncodingController {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileEncodingController.class);

  @GetMapping
  public String form(final Model model) {
    model.addAttribute(new Form());
    return "fileencodingform";
  }

  @PostMapping
  public String upload(@Validated @ModelAttribute final Form form, final BindingResult bindingResult,
      final RedirectAttributes redirectAttributes) throws IOException {

    if (bindingResult.hasErrors()) {
      return "fileencodingform";
    }

    final byte[] bytes = form.getFile().getBytes();

    final Charset charset = this.findCharset(bytes);

    LOGGER.info("Detected charset: {}", charset);
    LOGGER.info("Content read in UTF-8: {}", new String(bytes));
    LOGGER.info("Content read in {}: {}", charset, new String(bytes, charset));

    redirectAttributes.addFlashAttribute("detectedCharset", charset.name());

    return "redirect:/fileencoding";
  }

  private Charset findCharset(final byte[] bytes) {
    final CharsetDetector charsetDetector = new CharsetDetector();
    charsetDetector.setText(bytes);
    final CharsetMatch charsetMatch = charsetDetector.detect();

    if (charsetMatch == null) {
      throw new IllegalArgumentException("Not supported charset");
    }

    return Charset.forName(charsetMatch.getName());
  }

  public static class Form {

    @NotBlank
    private String name;

    private MultipartFile file;

    public void setName(final String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }

    public void setFile(final MultipartFile file) {
      this.file = file;
    }

    public MultipartFile getFile() {
      return this.file;
    }

  }

}
