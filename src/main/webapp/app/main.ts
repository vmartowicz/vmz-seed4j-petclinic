import { provideForOwners } from '@/owner/application/OwnersProvider';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { provideForVets } from '@/vet/application/VetsProvider';
import axios from 'axios';
import { createApp } from 'vue';
import AppVue from './AppVue.vue';
import router from './router';
// seed4j-needle-main-ts-import

const http = new AxiosHttp(axios.create());
provideForOwners(http);
provideForVets(http);

const app = createApp(AppVue);
app.use(router);
// seed4j-needle-main-ts-provider
app.mount('#app');
