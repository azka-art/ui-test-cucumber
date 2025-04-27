package com.automation.tests.runner;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "com.automation.tests.steps")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty, json:build/reports/cucumber.json, html:build/reports/cucumber.html")
public class CucumberTestRunner {
    // This class is empty, it's just a runner
}
