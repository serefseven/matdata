node {
    def app

    stage ('Clean workspace') {
        cleanWs()
    }

    stage ('Git Checkout') {

      git branch: 'master', credentialsId: '74ef8903-9236-47e4-b8ad-79f7e24157ed', url: 'https://github.com/serefseven/matdata.git'

    }

    stage('Remove Existing Image') {
        sh('docker image rmi matdata-be || (echo "Image matdata-be didn\'t exist so not removed."; exit 0)')
    }

    stage('Build image') {
        sh('docker build -t matdata-be . --no-cache')
    }

    stage('Remove Existing Container') {
        sh('docker rm -f matdata-be')
    }

    stage('Deploy') {
        sh('docker run -d --restart unless-stopped -p 8901:8080 --name matdata-be matdata-be')
    }

}
