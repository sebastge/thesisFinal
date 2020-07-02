# caseEV

To run this project you will need to have repast Simphony installed with its requirements.
See: https://repast.github.io/  For a guide on how to install it and Java.

To run casEV you will need Repast Simphony installed locally. Installation instruction can be found at: https://repast.github.io/

After installing Rapst Simphony, this repository needs to be cloned locally. After doing that, import the project using Eclipse IDE (w/Repast Simphony):

Right click package exporer window -> import -> General -> Existing projects into work space -> select root directory (navigate to the cloned the project) -> finish.)

To run the model, click the dropdown next to the green playbutton in Eclipse (run) and select "run configurations", select casEV and then press run. A window with the repast Simphony GUI should open, where you can enter parameters and run simulations.

If there are issues with memory management during the simulations, try the following:
Enter the run configurations as before, select casEV, then select the tab called "Arguments" and enter this string into the VM arguments: -Xms512M -Xmx1524M

If there are any questions or issues, please contact the author:

sebastge@stud.ntnu.no
seb.g.evans@gmail.com


After installing Rapst Simphony, this repository needs to be cloned locally. After doing that, import the project using Eclipse IDE (w/Repast Simphony):

![1](https://i.imgur.com/wYQHQXU.jpg)
