import react from '@vitejs/plugin-react';
import { visualizer } from 'rollup-plugin-visualizer';
import { defineConfig, loadEnv, splitVendorChunkPlugin } from 'vite';
import tsconfigPaths from 'vite-tsconfig-paths';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');

  const proxyTarget = `http://${env.BACKEND_SERVER}`;
  console.log('proxy target', proxyTarget);

  return {
    plugins: [react(), tsconfigPaths(), splitVendorChunkPlugin(), visualizer()],
    server: {
      host: true,
      port: 3000,
      proxy: {
        '/api': {
          target: proxyTarget,
          changeOrigin: true,
          secure: false,
          ws: true
        }
      }
    },
    build: {
      treeshake: true,
      minify: true,
      sourcemap: true,
      rollupOptions: {
        output: {
          manualChunks(id: string) {
            if (id.includes('react-router-dom') || id.includes('react-router')) {
              return '@react-router';
            }
            if (id.includes('fontawesome') || id.includes('free-solid') || id.includes('free-regular')) {
              return '@fontawesome';
            }
          }
        }
      }
    }
  };
});
