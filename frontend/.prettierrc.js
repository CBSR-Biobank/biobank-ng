const config = {
  trailingComma: 'es5',
  tabWidth: 2,
  useTabs: false,
  semi: true,
  singleQuote: true,
  printWidth: 120,
  tailwindConfig: './tailwind.config.js',
  tailwindFunctions: ['cn', 'clsx'],
  plugins: ['prettier-plugin-organize-imports', 'prettier-plugin-tailwindcss'],
};

export default config;
