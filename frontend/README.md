# Biobank-ng Frontend Project

This project contains the source code for **Biobank-ng**'s web-client. It is a [React](https://reactjs.org/)
application written in [TypeScript](https://www.typescriptlang.org/). It uses [Vite](https://vitejs.dev/) as a
build environment.

This project has the following dependencies:

* [React](https://react.dev/reference/react)

    React is a free and open-source front-end JavaScript library for building user interfaces based on
    components. It is maintained by Meta and a community of individual developers and companies.

* [TanStack Query](https://tanstack.com/query/latest)

    TanStack Query (FKA React Query) is often described as the missing data-fetching library for web
    applications, but in more technical terms, it makes fetching, caching, synchronizing and updating server
    state in your web applications a breeze.

* [React Router](https://reactrouter.com/en/main)

    React Router is a lightweight, fully-featured routing library for the React JavaScript library. React
    Router runs everywhere that React runs; on the web, on the server (using node.js), and on React Native.

* [React Hook Form](https://react-hook-form.com/)

    Performant, flexible and extensible forms with easy-to-use validation.

* [Zustand](https://docs.pmnd.rs/zustand/getting-started/introduction)

    A small, fast, and scalable bearbones state management solution. Zustand has a comfy API based on hooks.
    It isn't boilerplatey or opinionated, but has enough convention to be explicit and flux-like.

* [Zod](https://zod.dev/)

    Zod is a TypeScript-first schema declaration and validation library. I'm using the term "schema" to
    broadly refer to any data type, from a simple `string` to a complex nested object.

* [shadcn/ui](https://ui.shadcn.com/)

    Beautifully designed components that you can copy and paste into your apps. Accessible. Customizable. Open
    Source.

* [Tailwind CSS](https://tailwindcss.com/)

    Utility classes help you work within the constraints of a system instead of littering your stylesheets
    with arbitrary values. They make it easy to be consistent with color choices, spacing, typography,
    shadows, and everything else that makes up a well-engineered design system.

* [FontAwesome](https://fontawesome.com/v5/docs/web/use-with/react)

    Font Awesome is the Internet's icon library and toolkit, used by millions of designers, developers, and
    content creators.

* [Vite](https://vitejs.dev/guide/)

    Vite (French word for "quick", pronounced /vit/￼, like "veet") is a build tool that aims to provide a
    faster and leaner development experience for modern web projects.

## Development

### Install

Make sure you have the following packages / apps installed:

* [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* [Node](https://nodejs.org/en/download/package-manager/)

1. Use the following command to clone the project:

    ```sh
    git clone https://github.com/CBSR-Biobank/biobank-ng.git
    ```

    This command must be used inside the folder where you want to store the project files. For example: `$HOME/src/CBSR`.

2. Install the project dependencies:

    ```sh
    cd biobank-ng/frontend
    npm install
    ```

### Configure the backend

The application needs to communicate with a backend server. The DNS name and port number must be specified. To
do this, define the following environment variable in your shell:

```
export BACKEND_SERVER=https://biobank-training.cbsr.ualberta.ca:8443
```

In this example, the backend server runs at `biobank-training.cbsr.ualberta.ca` port 8443.

If you want to use a different backend server, replace `biobank-training.cbsr.ualberta.ca` with the DNS name
of your server. To use a port other than 80, append a colon and then the port number.

### Running in development mode

To run the application in development mode, use the following command at the project's root directory:

```sh
cd __project_root/biobank-ng/frontend
npm run dev
```

Where `__project_root__` is the name of the folder where you cloned the repository.

After this command starts, it will display a URL. Open this URL in your web browser and you will see the
application's homepage.

Usually, the URL is [http://127.0.0.1:3000/](http://127.0.0.1:3000/).

### Code Formatting

This project uses [Prettier](https://prettier.io/) to format the source code. Please install a Prettier plugin
in your code editor and configure it to run Prettier prior to saving a file.

The config file for prettier is here: [.prettierrc](https://github.com/CBSR-Biobank/biobank-ng/blob/main/frontend/.prettierrc).

#### VS Code

Here is an article on how to set up Prettier on VS Code:
[https://www.robinwieruch.de/how-to-use-prettier-vscode/](https://www.robinwieruch.de/how-to-use-prettier-vscode/).
