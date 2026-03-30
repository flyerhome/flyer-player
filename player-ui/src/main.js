import { createApp } from 'vue'
import {Row,Col,List,Form,Input} from 'ant-design-vue';
import App from './App.vue'
import './style.css'
createApp(App)
    .use(Row)
    .use(Col)
    .use(List)
    .use(Form)
    .use(Input)
    .mount('#app')