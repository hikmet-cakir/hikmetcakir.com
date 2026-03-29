import { defineConfig } from 'astro/config'; 
import node from '@astrojs/node';
 
export default defineConfig({ 
  base: '/admin',
  output: 'server', 
  adapter: node({ mode: 'standalone' }), 
  middleware: true,
  server: {
    port: 4001
  }
});
