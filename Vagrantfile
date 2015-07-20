
Vagrant.configure(2) do |config|
  config.vm.box = "parallels/ubuntu-14.04"

  config.vm.network "forwarded_port", guest: 9000, host: 9000
  config.vm.network "forwarded_port", guest: 35729, host: 35729

  config.vm.provision "shell", inline: <<-SHELL

    echo -e "\n--- Updating packages list ---\n"
    apt-get -qq update

    echo -e "\n--- Install base packages ---\n"
    apt-get -y install vim curl build-essential software-properties-common git screen

    echo -e "\n--- Add some repos to update our distro ---\n"
    curl -sL https://deb.nodesource.com/setup_0.12 | sudo bash -

    echo -e "\n--- Updating packages list ---\n"
    apt-get -qq update

    echo -e "\n--- Installing NodeJS and NPM ---\n"
    apt-get -y install nodejs

    echo -e "\n--- Installing frontend components ---\n"
    npm install -g grunt grunt-cli npm-check-updates yo bower generator-angular generator-webapp

    echo -e "\n--- Installing ruby components ---\n"
    sudo apt-get -y install ruby-full rubygems-integration
    sudo gem install sass
    sudo gem install compass

    ln -s /vagrant /home/vagrant

  SHELL


end