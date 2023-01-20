# Как выложить на сервер свой Mule Project?

1. Экспортировать в .jar файл проект. `File` &#8594; `Export` &#8594; `Select an export wizard: Mule`. (дефолтные галочки не убирать) - [cсылка][export]
2. Скачать и загрузить на сервер `Mule Standalone` - [ссылка][install]
3. После установки, можно проверить что работает запустив - [ссылка][start]
4. Дальше все файлы из каталога `target` вашего проекта и сам **.jar** файл закидываем в папку где установлен mule: `$MULE_HOME/apps/{you_project_name}`
5. После чего запускаете с помощью команды: `sudo $MULE_HOME/bin/mule`
6. Ваш проект должен будет задеплоиться.


[export]: https://docs.mulesoft.com/studio/7.5/import-export-packages
[install]: https://docs.mulesoft.com/mule-runtime/4.4/runtime-installation-task
[start]: https://docs.mulesoft.com/mule-runtime/4.4/starting-and-stopping-mule-esb