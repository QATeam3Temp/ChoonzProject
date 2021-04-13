package com.qa.choonz.cuke.runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/cuke/ReadFunction-guest.feature"}, monochrome = true,plugin = {"pretty", "html:src/test/resources/reports/cucumber-report.html", "json:src/test/resources/cucumber.json"})
public class Runner {

}
