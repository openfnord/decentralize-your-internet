// Description: This pipeline will update the server and reboot it
//
// click on → "This build is parameterized." 
// assign a name
// credential type → username and password
// under default value → "+ Add" → "Jenkins"
// Kind → Username and password
// assign a username and password (the password has to be the same as for the linux user "jenkins") → "Add"
// replace unimatrix_herdenadmin_pw from the script with the ID of your just created access data → "manage jenkins" → "Credentials"
//
// Recommendation: Run once a night
// H 4 * * *

pipeline {
    agent any

    stages {
        stage('update') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'unimatrix_herdenadmin_pw', usernameVariable: 'SUDO_USER', passwordVariable: 'SUDO_PW')]) {
                sh 'echo $SUDO_PW | sudo -S apt update'
                }
            }
        }
        stage('upgrade') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'unimatrix_herdenadmin_pw', usernameVariable: 'SUDO_USER', passwordVariable: 'SUDO_PW')]) {
                sh 'echo $SUDO_PW | sudo -S apt-get upgrade -y'
                }
            }
        }
        stage('reboot') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'unimatrix_herdenadmin_pw', usernameVariable: 'SUDO_USER', passwordVariable: 'SUDO_PW')]) {
                sh 'echo $SUDO_PW | sudo -S shutdown -r 1'
                }
            }
        }
    }
}
