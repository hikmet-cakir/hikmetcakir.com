import { defineConfig } from 'astro/config';
import tailwindcss from '@tailwindcss/vite';
import node from '@astrojs/node';
import sitemap from '@astrojs/sitemap';

export default defineConfig({
  site: 'https://hikmetcakir.com',
  output: 'server',
  vite: {
    plugins: [tailwindcss()]
  },
  adapter: node({ mode: 'standalone' }), 
  integrations: [sitemap()],
  server: {
    port: 4000
  }
});