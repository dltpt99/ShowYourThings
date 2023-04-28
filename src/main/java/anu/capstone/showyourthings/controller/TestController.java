package anu.capstone.showyourthings.controller;


import anu.capstone.showyourthings.service.BarcodeService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Controller
public class TestController {
    private final BarcodeService barcodeService;

    public TestController(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }

    @RequestMapping("/")
//    @ResponseBody
    public String index() {
        return "index";
    }

    @GetMapping("/barcode/{no}")
    @ResponseBody
    public String searchBarcode(@PathVariable("no") String no, Model model) {
//        API_result result = testService.getDataFromAPIByBarcode(no);

        String result = barcodeService.getDataFromAPIByBarcode(no);
        return result;

//        return result.getCMPNY_NM() + " " + result.getPRDT_NM();
    }

    @GetMapping("/barcode2/{no}")
    @ResponseBody
    public String testBarcode(@PathVariable("no") String no) {
        String result = barcodeService.testBarcode(no);
        return result;
    }



}
