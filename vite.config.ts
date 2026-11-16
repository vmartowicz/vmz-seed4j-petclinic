import vue from '@vitejs/plugin-vue';
import { fileURLToPath, URL } from 'node:url';
import { defineConfig } from 'vite';

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    tsconfigPaths: true,
    alias: {
      '@': fileURLToPath(new URL('./src/main/webapp/app', import.meta.url)),
    },
  },
  build: {
    outDir: '../../../target/classes/static',
  },
  root: 'src/main/webapp',
  server: {
    port: 9000,
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
});
