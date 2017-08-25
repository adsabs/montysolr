# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

$script = <<SCRIPT
export DEBIAN_FRONTEND=noninteractive 
sudo apt-get update
sudo apt-get install -y nodejs nodejs-legacy npm phantomjs
sudo npm install -g grunt-cli
cd /bumblebee
npm install # install the dependencies from package.json
grunt setup # setup the project (libraries)
sudo apt-get clean
sudo apt-get autoremove -y
sudo rm -rf /root/.cache/* /tmp/* /var/tmp/*
SCRIPT

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # VM 0
  config.vm.define :bumblebee do |bumblebee|
    # Define box
    bumblebee.vm.box = "ubuntu/xenial64"
    bumblebee.vm.network "forwarded_port", guest: 8000, host: 8000
    bumblebee.vm.provider "virtualbox" do |vb|
     vb.memory = "2048"
     vb.cpus = 2
    end
    # Provision
    bumblebee.vm.provision :shell, inline: $script
    # Share an additional folder to the guest VM
    bumblebee.vm.synced_folder ".", "/bumblebee/"
  end

end
