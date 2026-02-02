{
  description = "IPED - Digital Evidence Processor and Indexer";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};

        # Build dependency: Maven
        maven = pkgs.maven;

        # Runtime dependencies
        jdk = pkgs.jdk17;
        sleuthkit = pkgs.sleuthkit;

        # IPED Package
        iped = pkgs.stdenv.mkDerivation rec {
          pname = "iped";
          version = "4.4.0-SNAPSHOT";

          src = ./.;

          nativeBuildInputs = [ maven jdk pkgs.makeWrapper ];

          # NOTE: This build process requires network access to download Maven dependencies.
          # In a standard Nix sandbox, this will fail.
          # Usage: nix build --impure
          buildPhase = ''
            export HOME=$(pwd)
            mvn clean install -DskipTests -Dmaven.repo.local=$HOME/.m2/repository
          '';

          installPhase = ''
            mkdir -p $out/share/iped
            cp -r target/release/iped-${version}/* $out/share/iped/

            mkdir -p $out/bin

            # Use find to locate sleuthkit jar in the store
            TSK_JAR=$(find ${sleuthkit}/share/java -name "sleuthkit-*.jar" | head -n 1)

            # Wrapper script
            makeWrapper ${jdk}/bin/java $out/bin/iped \
              --add-flags "-jar $out/share/iped/iped.jar" \
              --set JAVA_HOME "${jdk}" \
              --set TSK_HOME "${sleuthkit}" \
              --add-flags "-Diped.tsk.jar.path=\$TSK_JAR" \
              --prefix PATH : "${sleuthkit}/bin"
          '';
        };

      in
      {
        packages.default = iped;

        devShells.default = pkgs.mkShell {
          buildInputs = [ maven jdk sleuthkit ];
          shellHook = ''
            export JAVA_HOME=${jdk}
            export TSK_HOME=${sleuthkit}
            export TSK_JAR=$(find ${sleuthkit}/share/java -name "sleuthkit-*.jar" | head -n 1)
            export IPED_TSK_JAR_PATH=$TSK_JAR
            echo "IPED Development Environment"
            echo "TSK_JAR found at: $TSK_JAR"
            echo "You can build with: mvn clean install -DskipTests"
            echo "And run with: java -Diped.tsk.jar.path=\$TSK_JAR -jar target/release/iped-*/iped.jar"
          '';
        };
      }
    );
}
