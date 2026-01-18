import { defineConfig } from 'astro/config';
import tailwindcss from '@tailwindcss/vite';
import node from '@astrojs/node';

export default defineConfig({
  vite: {
    plugins: [tailwindcss()]
  },
  adapter: node({ mode: 'standalone' }), 
  server: {
    port: 4000
  }
});