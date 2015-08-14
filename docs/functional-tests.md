We run functional tests either on:

  - local machine (much faster, but not so many browsers)
  - on SauceLabs and other servers


To install the local selenim on Linux (mindlessly follow these instructions):

I'm using Chrome here:

  ```
  #verify chrome binary exists (https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver)
  ls /usr/bin/google-chrome

  # install chromium webdriver, download the latest version from
  https://sites.google.com/a/chromium.org/chromedriver/downloads

  # unpack it into your system
  curl http://chromedriver.storage.googleapis.com/2.16/chromedriver_linux64.zip > /tmp/ch.zip; cd tmp; unzip /tmp/ch.zip; sudo mv chromedriver /usr/local/bin/

  # verify it is accessible in your path
  chromedriver -v
  ```


Useful docs explaining installation:
 - http://www.softwaretestinghelp.com/webdriver-eclipse-installation-selenium-tutorial-9/
 - LeadFoot API
  - https://theintern.github.io/leadfoot/Command.html