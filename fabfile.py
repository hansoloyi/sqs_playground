#!/usr/bin/env python2.7

from fabric.api import env, get, hosts, put, sudo, run
from fabric.context_managers import cd, settings
from fabric.contrib.project import rsync_project
import os


env.use_ssh_config = True

env.hosts = ['airflow-linear-optimizer.prod.dc3']

with open('version.sbt', 'r') as versionFile:
    version = versionFile.read().replace('\n', '').rsplit(':=')[1].strip().strip('"')

jar_filename = "sqs_playground-assembly-{0}.jar".format(version)
ln_jar_filename = "sqs_playground-core-assembly.jar"
app_path = "sqs_playground"
production_app_path = "sqs_playground"


def deploy():
    put("ingest/target/scala-2.11/{0}".format(jar_filename), "~/")
    sudo("mv {0} /home/spark/{1}/".format(jar_filename, app_path))
    sudo("chown spark:spark /home/spark/{0}/{1}".format(app_path, jar_filename))
    sudo("ln -sf {0} /home/spark/{1}/{2}".format(jar_filename, app_path, ln_jar_filename))

def _put_in_spark_home(src, dest_path, make_executable=False):
    basename = os.path.basename(src)
    dest = "/home/spark/{0}/{1}".format(dest_path, basename)
    put(src, "~/")
    sudo("mv {0} {1}".format(basename, dest))
    sudo("chown spark:spark {0}".format(dest))
    if make_executable:
        sudo("chmod +x {0}".format(dest))
    return dest


def _deploy_application(path, start_script):
    # Copy assembly jar
    _put_in_spark_home("target/scala-2.11/{0}".format(jar_filename), path)
    sudo("ln -sf {0} /home/spark/{1}/{2}".format(jar_filename, path, ln_jar_filename))

    # Copy start script
    _put_in_spark_home(start_script, path, make_executable=True)

@hosts('airflow-linear-optimizer.prod.dc3')
def deploy_user_test():
    user_app_path = "test-{0}/{1}".format(production_app_path, env.user)

    _deploy_application(user_app_path, "start-sqs_playground.sh")

