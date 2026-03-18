import { createApp } from 'vue'
import {Row,Col,List} from 'ant-design-vue';
import App from './App.vue'
import './style.css'
createApp(App)
    .use(Row)
    .use(Col)
    .use(List)
    .mount('#app')